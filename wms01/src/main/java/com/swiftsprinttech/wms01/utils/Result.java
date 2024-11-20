package com.swiftsprinttech.wms01.utils;


    public class Result<T> {

        // 状态：success 或 error
        private String status;
        // 响应信息
        private String message;
        // 响应数据
        private T data;

        // 构造方法
        public Result(String status, String message, T data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        // 成功的静态方法
        public static <T> Result<T> success(String message, T data) {
            return new Result<>("success", message, data);
        }

        // 成功的静态方法，无数据
        public static <T> Result<T> success(String message) {
            return new Result<>("success", message, null);
        }

        // 失败的静态方法
        public static <T> Result<T> error(String message, T data) {
            return new Result<>("error", message, data);
        }

        // 失败的静态方法，无数据
        public static <T> Result<T> error(String message) {
            return new Result<>("error", message, null);
        }

        // Getters and Setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

