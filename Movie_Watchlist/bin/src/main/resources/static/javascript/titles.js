titles = ["\"Mickey\"","\"The Kid\"","\"Arrival of a Train at La Ciotat\"", "\"The Big Swallow\"", "\"The Great Train Robbery\"", "\"The Birth of a Nation\"", "\"Sunrise: A Song of Two Humans\"", "\"Sir Arne’s Treasure\"", "\"It\"", "\"Earth\"", "\"Metropolis\"", "\"A Trip to the Moon\"", "\"The Cabbage Fairy\"", "\"The Battleship Potemkin\"", "\"The Passion of Joan of Arc\"", "\"Suspense\"", "\"The Lodger: A Story of the London Fog\"", "\"The Cabinet of Dr. Caligari\"", "\"The Phantom Carriage\"", "\"Witchcraft Through the Ages\"", "\"The Gold Rush\"", "\"The Gold Rush\"", "\"Sherlock, Jr.\"", "\"Safety Last!\"", "\"City Lights\"", "\"The General\"",  "\"The Wind\""];
var point = Math.floor(Math.random()*titles.length-1);

function changeText(){
  $('h1').html(titles[point]);
  if(point < ( titles.length - 1 )) {
    point++;
  } else {
	point = 0;
  } 
}

setInterval(changeText, 4000);
changeText();