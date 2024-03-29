// HTML5의 geolocation으로 사용할 수 있는지 확인합니다
if (navigator.geolocation) {

  // GeoLocation을 이용해서 접속 위치를 얻어옵니다
  navigator.geolocation.getCurrentPosition(function(position) {

    var lat = position.coords.latitude, // 위도
        lon = position.coords.longitude; // 경도

    var locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
        message = '<div style="padding:5px;">현재 위치</div>'; // 인포윈도우에 표시될 내용입니다

    // 마커와 인포윈도우를 표시합니다
    displayMarker(locPosition, message);

  });

} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다

  var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
      message = 'geolocation을 사용할수 없어요..'

  displayMarker(locPosition, message);
}

// 지도에 마커와 인포윈도우를 표시하는 함수입니다
function displayMarker(locPosition, message) {

  var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png', // 마커이미지의 주소입니다
      imageSize = new kakao.maps.Size(64, 69), // 마커이미지의 크기입니다
      imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

// 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
  var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption),
      markerPosition = new kakao.maps.LatLng(37.54699, 127.09598); // 마커가 표시될 위치입니다


  // 마커를 생성합니다
  var marker = new kakao.maps.Marker({
    map: map,
    position: locPosition,
    image: markerImage // 마커이미지 설정
  });

  // 마커가 지도 위에 표시되도록 설정합니다
  marker.setMap(map);

  var iwContent = message, // 인포윈도우에 표시할 내용
      iwRemoveable = true;

  // 인포윈도우를 생성합니다
  var infowindow = new kakao.maps.InfoWindow({
    content : iwContent,
    removable : iwRemoveable
  });

  // 인포윈도우를 마커위에 표시합니다
  infowindow.open(map, marker);

  // 지도 중심좌표를 접속위치로 변경합니다
  map.setCenter(locPosition);
}

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
      level: 8 // 지도의 확대 레벨
    };

mapContainer.style.width = '1200px';
mapContainer.style.height = '800px';

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// 마커를 표시할 위치와 내용을 가지고 있는 객체 배열입니다
var positions = [];

for (let i = 0; i < hasLibsData.length; i++) {
  const markerData = hasLibsData[i];

  const hasBook = markerData.hasBook;

  // iwPosition = new kakao.maps.LatLng(33.450701, 126.570667); //인포윈도우 표시 위치입니다


  if (hasBook === "Y") {
    const newMarker = {
      content: `<div>${markerData.libNm}</div>`,
      latlng: new kakao.maps.LatLng(markerData.libLa, markerData.libLo),
      url : markerData.libUrl
    };


    positions.push(newMarker);
  }
}

for (var i = 0; i < positions.length; i ++) {
  const markerData = positions[i];
  // 마커를 생성합니다
  var marker = new kakao.maps.Marker({
    map: map, // 마커를 표시할 지도
    position: positions[i].latlng, // 마커의 위치
    clickable: true
  });

  var iwContent = `<div style="padding:5px;">${markerData.content} <br><a href=${markerData.url} style="color:blue" target="_blank">홈페이지</a></div>`, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
      iwRemoveable = true;


  // 마커에 표시할 인포윈도우를 생성합니다
  var infowindow = new kakao.maps.InfoWindow({
    content: iwContent, // 인포윈도우에 표시할 내용
    removable : iwRemoveable
  });


  // 클로저를 이용하여 각 마커에 독립적인 인포윈도우를 설정합니다
  (function(marker, infowindow) {
    kakao.maps.event.addListener(marker, 'click', function() {
      // 마커 위에 인포윈도우를 표시합니다
      infowindow.open(map, marker);
    });
  })(marker, infowindow);
}

// 마커를 지도에 표시합니다.
marker.setMap(map);



