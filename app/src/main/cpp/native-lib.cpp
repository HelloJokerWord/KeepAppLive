#include <jni.h>
#include <string>
#include <filesystem>
#include <android/log.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>
#include <arpa/inet.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "WorkService", __VA_ARGS__)

void guard_start_work();
bool guard_create();
void guard_read_msg();

const char *userId;

/**
 * 用于创建一个守护进程
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_keepalivedemo_ndkservice_Guard_create(JNIEnv *env, jobject thiz, jstring user_id) {
    userId = env->GetStringUTFChars(user_id, 0);

    //开进程
    pid_t pid = fork();
    if (pid < 0) {
        LOGE("Guard开进程失败");
    } else if (pid == 0) {//子进程
        guard_start_work();
    } else if (pid > 0) {//父进程

    }

    env->ReleaseStringUTFChars(user_id, userId);
}

/**
 * 守护进程 - 开始
 */
void guard_start_work() {
    if (guard_create()) {
        guard_read_msg();
    }
}

const char *PATH = "/data/data/com.example.keepalivedemo.ndkservice/guard.socket";
int server_lfd = -1;
int server_cfd = -1;

/**
 * 守护进程 - 创建socket
 */
bool guard_create() {
    server_lfd = socket(AF_LOCAL, SOCK_STREAM, 0);
    if (server_lfd < 0) {
        LOGE("Guard socket 初始化错误");
        return false;
    }

    unlink(PATH);//把之前连接的服务端清空

    struct sockaddr_un addr;
    bzero(&addr, sizeof(sockaddr_un));
    addr.sun_family = AF_LOCAL;
    strcpy(addr.sun_path, PATH);
    int bind_res = bind(server_lfd, (const sockaddr *) &addr, sizeof(sockaddr_un));
    if (bind_res < 0) {
        LOGE("Guard bind错误");
        return false;
    }

    listen(server_lfd, 5);//可以守护5个app
    LOGE("Guard 开始listen");

    while (true) {
        server_cfd = accept(server_lfd, NULL, NULL);
        if (server_cfd < 0) {
            if (errno == EINTR) {
                //client连接失败，重连
                continue;
            } else {
                LOGE("Guard 读取错误");
                return 0;
            }
        } else {
            LOGE("进程 %d 连接上了 Guard", server_cfd);
            break;
        }
    }
    return true;
}

/**
 * 守护进程 - 阻塞读取
 */
void guard_read_msg() {
    LOGE("guard_read_msg");
    fd_set set;
    struct timeval timeout = {3, 0};//3秒超时
    while (true) {
        FD_ZERO(&set);
        FD_SET(server_cfd, &set);
        int select_res = select(server_cfd + 1, &set, NULL, NULL, &timeout);
        LOGE("select_res:%d", select_res);
        if (select_res > 0) {
            if (FD_ISSET(server_cfd, &set)) {//保证是指定apk的连接
                LOGE("userId: %d 断开", userId);
                char temp[256] = {0};
                //read是阻塞的，客户端断开之后才会往后执行
                int res = read(server_cfd, temp, sizeof(temp));
                LOGE("准备启动服务");
                //执行启动服务
                execlp("am", "am", "startservice", "--user", userId,"com.zyc.doubleguard/com.zyc.doubleguard.WorkService", (char *) NULL);
                LOGE("启动服务完成");
                break;
            }
        }
    }
}

/**
 * app连接守护进程
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_keepalivedemo_ndkservice_Guard_connect(JNIEnv *env, jobject thiz) {
    LOGE("app准备连接守护进程");
    int client_cfd;
    struct sockaddr_un addr;
    while (true) {
        client_cfd = socket(AF_LOCAL, SOCK_STREAM, 0);
        if (client_cfd < 0) {
            LOGE("app连接守护进程启动失败");
            return;
        }

        bzero(&addr, sizeof(sockaddr_un));
        addr.sun_family = AF_LOCAL;
        strcpy(addr.sun_path, PATH);
        int connect_res = connect(client_cfd, (const sockaddr *) &addr, sizeof(sockaddr_un));
        if (connect_res < 0) {
            //连不上就关闭后睡1秒重连
            LOGE("app连接守护进程失败");
            close(client_cfd);
            sleep(1);
        } else {
            LOGE("app连接守护进程成功");
            break;
        }
    }
}
