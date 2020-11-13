<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 06.11.2020
  Time: 1:08
  To change this template use File | Settings | File Templates.
--%>
function isEmpty(value)
{
    var val = value.trim();
    if (val.length == 0){
        return false;
    }
    return true;
}

function checkForm() {
    var allFilled = loginAndPasswordCorrect();
    allFilled = allFilled && isEmpty(document.getElementById("login").value);
    allFilled = allFilled && isEmpty(document.getElementById("password").value);
    if (allFilled){
        document.getElementById("submit").disabled = 0;
    }
    else{
        document.getElementById("submit").disabled = 1;
    }
}

function  loginAndPasswordCorrect(){
return !(/\W/.test(document.getElementById("login").value) || /\W/.test(document.getElementById("password").value));
}
