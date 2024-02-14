title = document.getElementById('main-content').cloneNode(true);

var tl = new TimelineMax({repeat:-1});

var dot;
  for (var i=0; i < 10; i++){
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