document.addEventListener('DOMContentLoaded', function() {
    var container = document.getElementById('map'),
        options = {
            center: new kakao.maps.LatLng(37.58163301627676, 126.8860228466823),
            level: 3
        };

    console.log('Options:', options);

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
    };

    // Ajax 요청을 통해 서버에서 메모 데이터를 가져와서 positions 배열에 추가
    // Ajax 요청을 통해 서버에서 메모 데이터를 가져와서 positions 배열에 추가
    function fetchMemoData() {
        var positions = []; // positions 배열을 정의

        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/memo/list', true);

        xhr.onload = function() {
            if (xhr.status >= 200 && xhr.status < 300) {
                var memoPage = JSON.parse(xhr.responseText);
                var memoList = memoPage.content;

                var memoListContainer = document.getElementById('memo-list');
                memoListContainer.innerHTML = ''; // 메모 리스트를 초기화

                memoList.forEach(function(memo) {
                    var position = {
                        content: memo.title,
                        latlng: new kakao.maps.LatLng(memo.latitude, memo.longitude)
                    };
                    positions.push(position);

                    // 메모를 리스트에 추가
                    var listItem = document.createElement('li');
                    listItem.textContent = memo.title + ' : ' + memo.content;
                    memoListContainer.appendChild(listItem);
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
                });

            } else {
                console.error('Failed to fetch memo data: ' + xhr.status);
            }
        };

        xhr.send();
    }


    // 메모 데이터 가져오기 실행
    fetchMemoData();

    var infowindow = new kakao.maps.InfoWindow();
});
