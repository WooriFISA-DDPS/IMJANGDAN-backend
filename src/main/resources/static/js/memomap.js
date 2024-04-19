// 메모 데이터를 가져오는 함수
function fetchMemoData() {
    axios.get('http://192.168.0.163:8989/memo/list')
    .then(response => {
        const memoData = response.data;
        displayMemos(memoData);
    })
    .catch(error => {
        console.error('Error fetching memo data:', error);
    });
}

// 메모 데이터를 화면에 표시하는 함수
function displayMemos(memoData) {
    const memoList = document.getElementById('memo-list');
    
    // 메모 데이터를 반복하여 메모를 동적으로 생성하고 추가
    memoData.forEach(memo => {
        // 각 메모를 표시하는 li 요소를 생성
        const memoItem = document.createElement('li');
        memoItem.textContent = `${memo.title} - ${memo.content} - ${memo.createdDate} - ${memo.writerName}`;
        
        // li 요소를 ul에 추가
        memoList.appendChild(memoItem);
    });
}

// 페이지가 로드될 때 메모 데이터를 가져오고 표시
window.addEventListener('load', fetchMemoData);
