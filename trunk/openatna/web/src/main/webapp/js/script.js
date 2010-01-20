function toggle(id) {
    var e = document.getElementById(id);
    if (e.style.display == 'block') {
        e.style.display = 'none';
        e.style.visibility = 'hidden';
        document.getElementById(id + "-link").innerHTML = "+";
    } else {
        e.style.display = 'block';
        e.style.visibility = 'visible';
        document.getElementById(id + "-link").innerHTML = "-";
    }
}