package com.computer.mazhihuapp.view;

/**
 * Created by computer on 2015/10/12.
 */
public class Order {


    /**
     * status : 1
     * error : 0
     * data : {"user_id":100001,"order_id":"207620150526184742539232","order_name":"test测试支付商品","price":0.1,"des":"商品描述","order_token":"hMPGI+5kJh7r/u7aaEe26CDHX9cjfm34hXZZoeidfEij62pjCPofJXPMdfAzSN2JtxgKn88URFxKrI4Nn5LGKuZeCjWMv6x9zUG+L0QyOj1CsowVB1fWxTgMw/tESw7D/Y01h2iKWFSRgbiTrz3wqmkFqmweTEgwfq3jg=","pay_status":0,"cp_order_id":"80094f4c68a8b0aaab150d953c70dabd"}
     */

    private int status;
    private int error;
    private DataEntity data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public int getError() {
        return error;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * user_id : 100001
         * order_id : 207620150526184742539232
         * order_name : test测试支付商品
         * price : 0.1
         * des : 商品描述
         * order_token : hMPGI+5kJh7r/u7aaEe26CDHX9cjfm34hXZZoeidfEij62pjCPofJXPMdfAzSN2JtxgKn88URFxKrI4Nn5LGKuZeCjWMv6x9zUG+L0QyOj1CsowVB1fWxTgMw/tESw7D/Y01h2iKWFSRgbiTrz3wqmkFqmweTEgwfq3jg=
         * pay_status : 0
         * cp_order_id : 80094f4c68a8b0aaab150d953c70dabd
         */

        private int user_id;
        private String order_id;
        private String order_name;
        private double price;
        private String des;
        private String order_token;
        private int pay_status;
        private String cp_order_id;

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public void setOrder_name(String order_name) {
            this.order_name = order_name;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public void setOrder_token(String order_token) {
            this.order_token = order_token;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }

        public void setCp_order_id(String cp_order_id) {
            this.cp_order_id = cp_order_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getOrder_name() {
            return order_name;
        }

        public double getPrice() {
            return price;
        }

        public String getDes() {
            return des;
        }

        public String getOrder_token() {
            return order_token;
        }

        public int getPay_status() {
            return pay_status;
        }

        public String getCp_order_id() {
            return cp_order_id;
        }
    }
}
