document.addEventListener('DOMContentLoaded', function() {
    var container = document.getElementById('map');
    var options = {
        center: new kakao.maps.LatLng(37.58163301627676, 126.8860228466823),
        level: 3
    };

    var map = new kakao.maps.Map(container, options);
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
    }

    var positions = [
        {
            content: '<div>카카오</div>',
            latlng: new kakao.maps.LatLng(33.450705, 126.570677)
        }
    ];

    for (var i = 0; i < positions.length; i ++) {
        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            map: map, // 마커를 표시할 지도
            position: positions[i].latlng // 마커의 위치
        });

        // 마커에 표시할 인포윈도우를 생성합니다
        var infowindow = new kakao.maps.InfoWindow({
            content: positions[i].content // 인포윈도우에 표시할 내용
        });

        // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
        // 이벤트 리스너로는 클로저를 만들어 등록합니다
        // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
        kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
        kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
    }

// 인포윈도우를 표시하는 클로저를 만드는 함수입니다
    function makeOverListener(map, marker, infowindow) {
        return function() {
            infowindow.open(map, marker);
        };
    }

// 인포윈도우를 닫는 클로저를 만드는 함수입니다
    function makeOutListener(infowindow) {
        return function() {
            infowindow.close();
        };
    }


});
