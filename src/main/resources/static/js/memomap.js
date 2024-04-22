document.addEventListener('DOMContentLoaded', function() {
    var mapContainer = document.getElementById('map');
    var mapOptions = {
        center: new kakao.maps.LatLng(37.58163301627676, 126.8860228466823), // 올바른 표기로 수정
        level: 3
    };

    var map = new kakao.maps.Map(mapContainer, mapOptions);

    var marker = new kakao.maps.Marker({
        position: map.getCenter()
    });
    marker.setMap(map);

    var currentLat, currentLng;


    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
        var latlng = mouseEvent.latLng;
        marker.setPosition(latlng);

        currentLat = latlng.getLat();
        currentLng = latlng.getLng();

        var message = '클릭한 위치의 위도는 ' + currentLat + ' 이고, ';
        message += '경도는 ' + currentLng + ' 입니다';
        document.getElementById('clickLatlng').innerHTML = message;
    });

    window.addButtonClick = function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                const latitude = currentLat;
                const longitude = currentLng;
                window.location.href = `http://localhost:3000/homememo?lat=${latitude}&lng=${longitude}`;
            }, function(error) {
                alert("위치 정보를 가져올 수 없습니다: " + error.message);
                window.location.href = 'http://localhost:3000/homememo';
            });
        } else {
            alert("Geolocation is not supported by this browser.");
        }
    };

    var positions = [
        {
            content: '<div>상암 IT 타워</div>',
            latlng: new kakao.maps.LatLng(37.58163301627676, 126.8860228466823)
        },
        {
            content: '<div>LG 유플러스</div>',
            latlng: new kakao.maps.LatLng(37.58057831675249, 126.88781319354182)
        },
        {
            content: '<div>가온 문화 공원</div>',
            latlng: new kakao.maps.LatLng(37.58191615523997, 126.88766966937938)
        }

    ];

    var infowindow = new kakao.maps.InfoWindow({
        map: map
    });

    positions.forEach(function(pos) {
        var marker = new kakao.maps.Marker({
            map: map,
            position: pos.latlng
        });

        kakao.maps.event.addListener(marker, 'click', function() {
            infowindow.setContent(pos.content);
            infowindow.open(map, marker);
        });

        infowindow.close();

    });

    // 2. DOMContentLoaded 이벤트 핸들러 위치 확인하기
    console.log('DOMContentLoaded 이벤트 핸들러가 실행되었습니다.');

    // 3. kakao.maps.LatLng에 전달되는 값을 확인하기
    console.log('LatLng 확인:', positions.map(pos => [pos.latlng.getLat(), pos.latlng.getLng()]));



});
