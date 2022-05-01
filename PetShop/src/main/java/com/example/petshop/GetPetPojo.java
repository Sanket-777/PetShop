package com.example.petshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPetPojo {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("petlist")
    @Expose
    private List<Petlist> petlist = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<Petlist> getPetlist() {
        return petlist;
    }

    public void setPetlist(List<Petlist> petlist) {
        this.petlist = petlist;
    }

    public class Petlist {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("pettype")
        @Expose
        private String pettype;
        @SerializedName("petname")
        @Expose
        private String petname;
        @SerializedName("petage")
        @Expose
        private String petage;
        @SerializedName("petcost")
        @Expose
        private String petcost;
        @SerializedName("petgender")
        @Expose
        private String petgender;
        @SerializedName("petimage")
        @Expose
        private String petimage;
        @SerializedName("usermobile")
        @Expose
        private String usermobile;
        @SerializedName("datee")
        @Expose
        private String datee;
        @SerializedName("expdate")
        @Expose
        private String expdate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPettype() {
            return pettype;
        }

        public void setPettype(String pettype) {
            this.pettype = pettype;
        }
        public String getPetType(){
            return pettype;
        }

        public String getPetname() {
            return petname;
        }

        public void setPetname(String petname) {
            this.petname = petname;
        }

        public String getPetage() {
            return petage;
        }

        public void setPetage(String petage) {
            this.petage = petage;
        }

        public String getPetcost() {
            return petcost;
        }

        public void setPetcost(String petcost) {
            this.petcost = petcost;
        }

        public String getPetgender() {
            return petgender;
        }

        public void setPetgender(String petgender) {
            this.petgender = petgender;
        }

        public String getPetimage() {
            return petimage;
        }

        public void setPetimage(String petimage) {
            this.petimage = petimage;
        }

        public String getUsermobile() {
            return usermobile;
        }

        public void setUsermobile(String usermobile) {
            this.usermobile = usermobile;
        }

        public String getDatee() {
            return datee;
        }

        public void setDatee(String datee) {
            this.datee = datee;
        }

        public String getExpdate() {
            return expdate;
        }

        public void setExpdate(String expdate) {
            this.expdate = expdate;
        }

    }
}
