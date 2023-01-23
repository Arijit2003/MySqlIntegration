<?php
$connect=mysqli_connect("localhost","root","");
mysqli_select_db($connect,"studentguidance");
$user=$_POST["user"];
$qry="DELETE FROM facultyinfo where emailId='$user'";
$res=mysqli_query($connect,$qry);
if($res==true){
    echo json_encode("deleted");
}
else{
    echo json_encode("not yet deleted");
}

?>