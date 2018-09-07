package com.sand_corporation.www.uthao.CloudFireStoreDataModel;

/**
 * Created by HP on 12/14/2017.
 */


public class CustomerBasicInfo {

    private String Customer_Age_Range, Customer_BirthDate, Customer_Email, Customer_FaceBookUrlLink,
            Customer_FaceBook_Details, Customer_FaceBook_PP, Customer_Gender, Customer_Mobile,
            Customer_Name,Customer_Profile_Pic_Url;

    public CustomerBasicInfo(String customer_Age_Range, String customer_BirthDate,
                             String customer_Email, String customer_FaceBookUrlLink,
                             String customer_FaceBook_Details, String customer_FaceBook_PP,
                             String customer_Gender, String customer_Mobile, String customer_Name,
                             String Customer_Profile_Pic_Url) {
        this.Customer_Age_Range = customer_Age_Range;
        this.Customer_BirthDate = customer_BirthDate;
        this.Customer_Email = customer_Email;
        this.Customer_FaceBookUrlLink = customer_FaceBookUrlLink;
        this.Customer_FaceBook_Details = customer_FaceBook_Details;
        this.Customer_FaceBook_PP = customer_FaceBook_PP;
        this.Customer_Gender = customer_Gender;
        this.Customer_Mobile = customer_Mobile;
        this.Customer_Name = customer_Name;
        this.Customer_Profile_Pic_Url = Customer_Profile_Pic_Url;
    }

    public CustomerBasicInfo() {

    }

    public String getCustomer_Profile_Pic_Url() {
        return Customer_Profile_Pic_Url;
    }

    public String getCustomer_Age_Range() {
        return Customer_Age_Range;
    }

    public String getCustomer_BirthDate() {
        return Customer_BirthDate;
    }

    public String getCustomer_Email() {
        return Customer_Email;
    }

    public String getCustomer_FaceBookUrlLink() {
        return Customer_FaceBookUrlLink;
    }

    public String getCustomer_FaceBook_Details() {
        return Customer_FaceBook_Details;
    }

    public String getCustomer_FaceBook_PP() {
        return Customer_FaceBook_PP;
    }

    public String getCustomer_Gender() {
        return Customer_Gender;
    }

    public String getCustomer_Mobile() {
        return Customer_Mobile;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }
}
