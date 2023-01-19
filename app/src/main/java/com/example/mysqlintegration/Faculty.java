package com.example.mysqlintegration;

public class Faculty{
    public String image;
    public String name;
    public String designation;
    public String department;
    public String cabinNo;
    public String email;
    public Boolean isSelected;


    Faculty(String image, String name, String designation,String department,String cabinNo,String email){
        this.image=image;
        this.name=name;
        this.designation=designation;
        this.department=department;
        this.cabinNo=cabinNo;
        this.email=email;
        this.isSelected=false;





    }

}
//data class Faculty(val image:String,val name:String,val designation:String,
//                   val department:String,val cabinNo:String,val email:String)