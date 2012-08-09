function toggle(id) {
    var state = document.getElementById(id).style.display;
            if (state == 'block') {
                document.getElementById(id).style.display = 'none';
                document.getElementById(id+'_less').style.display = 'none';
                document.getElementById(id+'_more').style.display = 'block';
            } else {
                document.getElementById(id).style.display = 'block';
                document.getElementById(id+'_more').style.display = 'none';
                document.getElementById(id+'_less').style.display = 'block';
            }
}