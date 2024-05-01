document.addEventListener('DOMContentLoaded', function() {
    var memoComponent = document.getElementById('memo-component');
    var newsComponent = document.getElementById('news-component');

    function updateNewsPosition() {
        var memoHeight = memoComponent.offsetHeight;
        newsComponent.style.top = (memoHeight + 60) + 'px'; // 메모 컴포넌트 아래쪽에 위치하도록 설정
    }

    // 초기 로드시 뉴스 컴포넌트 위치 업데이트
    updateNewsPosition();

    // 창 크기가 변경될 때마다 뉴스 컴포넌트 위치 업데이트
    window.addEventListener('resize', updateNewsPosition);
});



document.addEventListener('DOMContentLoaded', function() {
    var container = document.getElementById('map'),
        options = {
            center: new kakao.maps.LatLng(37.58163301627676, 126.8860228466823),
            level: 3
        };

    console.log('Options:', options);

    var map = new kakao.maps.Map(container, options);

    var infowindow = new kakao.maps.InfoWindow();

    var goodMarkers = [];
    var sosoMarkers = [];
    var badMarkers = [];

    var currentLat, currentLng;
    var marker = new kakao.maps.Marker(); // 마커 변수를 전역으로 선언

    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
            var latlng = mouseEvent.latLng;
            marker.setPosition(latlng);

            // 이전 마커를 지도에서 제거합니다.
            marker.setMap(null);

            // 새로운 마커를 생성합니다.
            marker = new kakao.maps.Marker({
                position: latlng
            });

            // 생성된 마커를 지도에 표시합니다.
            marker.setMap(map);

            currentLat = latlng.getLat();
            currentLng = latlng.getLng();

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

    // Ajax 요청을 통해 서버에서 메모 데이터를 가져와서 마커 배열에 추가
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
                    // 각 메모에 클릭 이벤트 추가
                    listItem.onclick = function() {
                        showMemoDetail(memo.id);
                    };
                    memoListContainer.appendChild(listItem);
                    console.log('category : ', memo.category);

                    // 메모의 카테고리에 따라 마커를 생성하여 해당하는 배열에 추가합니다.
                    if (memo.category === 'Good') {
                        createMarker(position, 'good');
                    } else if (memo.category === 'SoSo') {
                        createMarker(position, 'soso');
                    } else if (memo.category === 'Bad') {
                        createMarker(position, 'bad');
                    }
                });

            } else {
                console.error('Failed to fetch memo data: ' + xhr.status);
            }
        };

        xhr.send();
    }

    function showMemoDetail(memoId) {
        window.location.href = `http://localhost:3000/homememo?lat=${latitude}&lng=${longitude}`;
        // 예를 들어, window.location.href = 'memo_detail.php?id=' + memoId;
    }

    // 마커 생성 함수
    function createMarker(position, category) {
        var markerImageSrc = '';  // 마커이미지의 주소입니다. 스프라이트 이미지 입니다
        var imageSize = new kakao.maps.Size(40, 40); // 마커 이미지 크기
        var imageOption = { offset: new kakao.maps.Point(30, 30) }; // 마커 이미지 옵션

        // 마커 이미지 설정
        if (category === 'good') {
            markerImageSrc = '/images/good.png';
        } else if (category === 'soso') {
            markerImageSrc = '/images/soso.png';
        } else if (category === 'bad') {
            markerImageSrc = '/images/bad.png';
        }

        var markerImage = new kakao.maps.MarkerImage(markerImageSrc, imageSize, imageOption);

        var marker = new kakao.maps.Marker({
            position: position.latlng,
            image: markerImage,
            clickable: true
        });

        // 생성된 마커를 지도에 표시
        marker.setMap(map);

        var iwContent = `<div> ${position.content} <div>`,
            iwRemoveable = true;

// 인포윈도우를 생성합니다
        var infowindow = new kakao.maps.InfoWindow({
            content : iwContent,
            removable : iwRemoveable
        });

// 마커에 클릭이벤트를 등록합니다
        kakao.maps.event.addListener(marker, 'click', function() {
            // 마커 위에 인포윈도우를 표시합니다
            infowindow.open(map, marker);
        });


        // 해당 카테고리의 마커 배열에 추가
        if (category === 'good') {
            goodMarkers.push(marker);
        } else if (category === 'soso') {
            sosoMarkers.push(marker);
        } else if (category === 'bad') {
            badMarkers.push(marker);
        }
    }

    // 메모 데이터 가져오기 실행
    fetchMemoData();

    document.getElementById('allMenu').addEventListener('click', function() {
        // 나쁨 카테고리 클릭 시 모든 마커 지도에 표시
        setMarkersOnMap(goodMarkers, map);
        setMarkersOnMap(sosoMarkers, map);
        setMarkersOnMap(badMarkers, map);
    });

    // 좋음, 보통, 나쁨 카테고리 클릭 이벤트 핸들러
    document.getElementById('goodMenu').addEventListener('click', function() {
        // 좋음 카테고리 클릭 시 좋음 마커만 지도에 표시
        setMarkersOnMap(goodMarkers, map);
        setMarkersOnMap(sosoMarkers, null);
        setMarkersOnMap(badMarkers, null);
    });

    document.getElementById('sosoMenu').addEventListener('click', function() {
        // 보통 카테고리 클릭 시 보통 마커만 지도에 표시
        setMarkersOnMap(goodMarkers, null);
        setMarkersOnMap(sosoMarkers, map);
        setMarkersOnMap(badMarkers, null);
    });

    document.getElementById('badMenu').addEventListener('click', function() {
        // 나쁨 카테고리 클릭 시 나쁨 마커만 지도에 표시
        setMarkersOnMap(goodMarkers, null);
        setMarkersOnMap(sosoMarkers, null);
        setMarkersOnMap(badMarkers, map);
    });

    // 마커 배열을 지도에 표시하거나 숨기는 함수
    function setMarkersOnMap(markers, map) {
        markers.forEach(function(marker) {
            marker.setMap(map);
        });
    }
});
