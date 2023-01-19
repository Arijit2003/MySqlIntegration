<?php
#Atfirst connection string
$connect=mysqli_connect("localhost","root","");
mysqli_select_db($connect,"studentguidance");
$email=trim($_POST["email"]);
$password=md5(trim($_POST["password"]));
#query
$qry="SELECT * from `signup` where email='$email' and signup.password='$password'";
$raw=mysqli_query($connect,$qry);
$count=mysqli_num_rows($raw);
if($count>0){
    echo "found";
}
else{
    echo "not found";
}
?>