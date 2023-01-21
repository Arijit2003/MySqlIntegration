<?php
#Atfirst connection string
$connect=mysqli_connect("localhost","root","");
mysqli_select_db($connect,"studentguidance");
$name=trim($_POST['name']);
$email=trim($_POST['email']);
$password=md5(trim($_POST['password']));
$qry="SELECT * from `signup` where email= '$email' ";
$result=mysqli_query($connect,$qry);
$count=mysqli_num_rows($result);
if($count>0){
    $feedback='exist';
    echo $feedback;
}
else{
    $qry2="INSERT into `signup` VALUES(null,'$name','$email','$password')";
    $response=mysqli_query($connect,$qry2);
    if($response==TRUE){
        $feedback='inserted';
        echo $feedback;
    }
    else{
        $feedback='failed';
        echo $feedback;
    }
}


?>