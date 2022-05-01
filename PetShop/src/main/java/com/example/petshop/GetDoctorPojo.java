package com.example.petshop;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDoctorPojo {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("doctorlist")
    @Expose
    private List<Doctorlist> doctorlist = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<Doctorlist> getDoctorlist() {
        return doctorlist;
    }

    public void setDoctorlist(List<Doctorlist> doctorlist) {
        this.doctorlist = doctorlist;
    }

    public class Doctorlist {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("doctorphoto")
        @Expose
        private String doctorphoto;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDoctorphoto() {
            return doctorphoto;
        }

        public void setDoctorphoto(String doctorphoto) {
            this.doctorphoto = doctorphoto;
        }

    }
}