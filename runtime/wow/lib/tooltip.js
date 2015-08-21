function showTip(img, tip)
{ tiptext = document.getElementById(tip);
  tiptext.style.visibility = 'visible';
  tipimg = document.getElementById(img);
  tiptext.style.left = tipimg.offsetLeft + 25;
  tiptext.style.top = tipimg.offsetTop;
}
function hideTip(tip)
{ tiptext = document.getElementById(tip);
  tiptext.style.visibility = 'hidden';
}
