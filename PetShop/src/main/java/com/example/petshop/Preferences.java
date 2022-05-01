package com.example.petshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class Preferences
{

    SharedPreferences sharePref;
    SharedPreferences.Editor editor;

    public Preferences(Context context) {
        sharePref= PreferenceManager.getDefaultSharedPreferences(context);
        editor=sharePref.edit();
    }

    public void clearPreferences()
    {
        editor.clear();
        editor.commit();
    }

    public void  set_is_loggedin(String b) {
        editor.putString("LOGGEDIN",b);
        editor.commit();
    }
    public String get_is_loggedin()
    {
        return sharePref.getString("LOGGEDIN","");
    }

    //method to save order
    public void setOrder(String order) {
        editor.putString("ORDER", order);
        editor.commit();
    }

    //method for getting order
    public String getOrder() {
        return sharePref.getString("ORDER", "");
    }

    public void  setUserID(String userID) {
        editor.putString("USERID",userID);
        editor.commit();
    }
    public String getUserID()
    {
        return sharePref.getString("USERID","");
    }

    public void  setUserName(String userName) {
        editor.putString("USERNAME",userName);
        editor.commit();
    }
    public String getUserName()
    {
        return sharePref.getString("USERNAME","");
    }

    public void  setUserMobile(String userMobile) {
        editor.putString("USERMOBILE",userMobile);
        editor.commit();
    }
    public String getUserMobile()
    {
        return sharePref.getString("USERMOBILE","");
    }

    public void  setUserEmail(String userEmail) {
        editor.putString("USEREMAIL",userEmail);
        editor.commit();
    }
    public String getUserEmail()
    {
        return sharePref.getString("USEREMAIL","");
    }

    public void  setUserType(String userType) {
        editor.putString("USERTYPE",userType);
        editor.commit();
    }
    public String getUserType()
    {
        return sharePref.getString("USERTYPE","");
    }

    public void setProfilePhoto(String profileimg){
        editor.putString("PROFILEIMG",profileimg);
        editor.commit();
    }
    public String getProfilePhoto(){ return sharePref.getString("PROFILEIMG","");
    }

    public void setAddress(String address){
        editor.putString("ADDRESS",address);
        editor.commit();
    }
    public String getAddress()
    {
        return sharePref.getString("ADDRESS","");
    }
    public void setCity(String city){
        editor.putString("CITY",city);
        editor.commit();
    }
    public String getCity(){ return sharePref.getString("CITY","");
    }

}
