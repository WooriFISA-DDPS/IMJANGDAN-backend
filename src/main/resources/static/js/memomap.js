document.addEventListener('DOMContentLoaded', function() {
    var container = document.getElementById('map'),
        options = {
            center: new kakao.maps.LatLng(37.58163301627676, 126.8860228466823),
            level: 3
        };

    console.log('Options:', options);

    var map = new kakao.maps.Map(container, options);

    var currentLat, currentLng;
    var marker = new kakao.maps.Marker(); // 마커 변수를 전역으로 선언

    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
        var latlng = mouseEvent.latLng;
        marker.setPosition(latlng);

        currentLat = latlng.getLat();
        currentLng = latlng.getLng();

        var message = '클릭한 위치의 위도는 ' + currentLat + ' 이고, ';
        message += '경도는 ' + currentLng + ' 입니다';
        document.getElementById('clickLatlng').innerHTML = message;

        // 마커 생성
        createMarker(latlng);
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
                    listItem.textContent = memo.category + ' : ' + memo.title + ' : ' + memo.content;
                    memoListContainer.appendChild(listItem);
                    console.log('category : ', memo.category);


                    var imageSrc;
                    if (memo.category === 'Good') {
                        imageSrc = '/images/good-icon.png';
                    } else if (memo.category === 'SoSo') {
                        imageSrc = '/images/soso-icon.jpeg';
                    } else if (memo.category === 'Bad') {
                        imageSrc = '/images/bad-icon.jpeg';
                    }

                    imageSize = new kakao.maps.Size(43, 46), // 마커이미지의 크기입니다
                    imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

                    // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
                    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

                    var marker = new kakao.maps.Marker({
                        position: position.latlng,
                        image: markerImage // 마커 이미지 설정
                    });

                    marker.setMap(map);

                    kakao.maps.event.addListener(marker, 'click', function() {
                        infowindow.setContent(position.content);
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

    // 마커 생성 함수
    function createMarker(position) {
        marker.setMap(null); // 기존 마커 삭제
        marker = new kakao.maps.Marker({
            position: position
        });
        marker.setMap(map); // 새로운 마커 지도에 표시
    }
});
