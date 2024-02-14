title = document.getElementById('main-title').cloneNode(true);
document.querySelector('.titleCont').appendChild(title);
title.classList.add("overTitle")
var line = document.createElement('div');
line.className = 'line';
document.getElementById('main-content').appendChild(line); 

var tl = new TimelineMax({repeat:-1});

for(var i=50; i--;){
  tl.to(title,R(0.03,0.17),{opacity:R(0,1),y:R(-1.5,1.5), x:R(-1.5,1.5)})
};

tl.to(line,tl.duration()/2,{opacity:R(0.1,1),x:R(-window.innerWidth/2,window.innerWidth/2),ease:RoughEase.ease.config({strength:0.5,points:10,randomize:true,taper: "none"}),repeat:1},0);

  var dot;
  for (var i=0; i < 11; i++){
    dot = document.createElement('div');
    dot.className = 'dot';
    document.getElementById('main-content').prepend(dot); 
    setDotPosition(dot);
    tl.to(dot,0.1,{opacity:0,repeat:1, yoyo:true, onComplete:setDotPosition, onCompleteParams:[dot], ease:RoughEase.ease.config({strength:0.5,points:10,randomize:true,taper: "none"})},0);
  }

function setDotPosition(dot)
{
  TweenMax.set(dot, {x:R(-window.innerWidth/2,window.innerWidth/2),y:R(-window.innerHeight,window.innerHeight), delay:R(0, 1)});
}

function R(max,min){return Math.random()*(max-min)+min};