<?php
# atfirst set the connection string
$connect=mysqli_connect("localhost","root","");
mysqli_select_db($connect,"studentguidance");
$name=$_POST["name"];
$designation=$_POST["designation"];
$department=$_POST["department"];
$cabinno=$_POST["cabinno"];
$emailId=$_POST["emailId"];
$image=$_POST["image"];

# generate a file name
$filename="IMG".rand().".jpg";
file_put_contents("IMAGES/".$filename,base64_decode($image));
#query
$qry="INSERT INTO `facultyinfo` VALUES(NULL,'$name','$designation','$department','$cabinno','$emailId','$filename')";
$res=mysqli_query($connect,$qry);
if($res==TRUE)
echo "inserted";
else
echo "failed";
?>