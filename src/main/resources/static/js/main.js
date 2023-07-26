document.querySelector('#search-input').addEventListener('keydown',
    function (event) {
      if (event.keyCode === 13) {
        searchBook();
      }
    });

document.querySelector('#search-input-btn').addEventListener('click',
    function (event) {
      searchBook();
    });

document.querySelector('#search-input').addEventListener('focus',
    function (event) {
      this.value = '';
    });


function addMetaHtml(meta) {
  let query = $('#search-input').val()

  return `<div id="book-box" class="row gx-4 gx-lg-5 align-items-center my-5">
                <div class="col-lg-7">
                    <p>[${query}] 에 대한 도서 검색결과  <br>(검색 속도: ${meta.searchTime}초)</p>
                    <p style="color: #636464">[대출 횟수는 5년간 서울 도서관 전체에서 합산된 대출 횟수]</p>  
                </div>
       
            </div>`
}

function addHTML(book) {
  return `<div id="book-box" class="row gx-4 gx-lg-5 align-items-center my-5">
                <div class="col-lg-2">
                  <img id="book-img" class="img-fluid rounded mb-4 mb-lg-0" src="${book.bookImg}" alt="Book image"</>
                </div>
                <div class="col-lg-5">
                    <h2 class="font-weight-light">${book.title}</h2>
                  <p>대출 횟수 : ${book.loanCnt}</p>
                  <p>저자 : ${book.author}</p>
                    <p>${book.content}</p>
                </div>
              <div class="col-lg-2">
                <a class="btn btn-outline-success" data-isbn="${book.isbn}">대출 가능 <br>도서관 찾기!</a>
              </div>
            </div>`
}

function openPopup_MapBook(isbn, lat, lon) {
  // 데이터 객체에 isbn, lat, lon 값을 추가
  const data = {
    isbn: isbn,
    lat: lat,
    lon: lon
  };

  $.ajax({
    type: 'POST',
    url: `/books/mapBook/search`,
    contentType: "application/json",
    data: JSON.stringify(data), // 수정된 data 객체를 JSON 형태로 변환하여 전송
    success: function (response) {
      // 새로운 팝업 창을 열고 응답을 받은 HTML로 내용을 채움
      const popupWindow = window.open('', 'areaCdInfo',
          'width=1200,height=800');
      popupWindow.document.write(response);
      popupWindow.document.close();
    },
    error(error) {
      console.error(error);
    }
  });

  return false;
}

function searchBook() {

  let query = $('#search-input').val().trim();

  // 2. 검색창 입력값을 검사하고, 입력하지 않았을 경우 focus.
  if (query == '') {
    alert('검색어를 입력해주세요');
    $('#search-input').focus();
    return;
  }
  $.ajax({
    type: 'GET',
    url: `/books/search?query=${query}`,
    success: function (response) {
      $('#book_container').empty();

      let meta = response.meta;
      let tempMetaHtml = addMetaHtml(meta);
      $('#book_container').append(tempMetaHtml);

      let books = response.documents;

      for (let i = 0; i < books.length; i++) {
        let book = books[i];
        let tempHtml = addHTML(book);
        $('#book_container').append(tempHtml);
      }

      // Ajax 호출이 성공적으로 끝나고 책의 목록을 화면에 그린 후에 스크롤을 맨 위로 이동
      window.scrollTo(0, 0);
    },
    error(error) {
      if (error.status === 400) {
        alert("잘못된 검색어입니다. 다시 입력해 주세요.");
        $('#search-input').focus();
      } else {
        console.error(error);
      }
    }
  })
}

$('#search-input').autocomplete({
  source: function (request, response) { //source: 입력시 보일 목록
    $.ajax({
      url: "/books/autocomplete"
      , type: "POST"
      , dataType: "JSON"
      , data: {query: request.term} // 검색 키워드
      , success: function (books) {  // 성공
        response(
            $.map(books, function (book) {
              return {
                label: book.title   // 목록에 표시되는 값
              };
            })
        );    //response
      }
      , error: function () { //실패
        console.log("오류가 발생했습니다.");
      }
    });
  }
  , focus: function (event, ui) { // 방향키로 자동완성단어 선택 가능하게 만들어줌
    return false;
  }
  , minLength: 2// 최소 글자수
  , autoFocus: false // true == 첫 번째 항목에 자동으로 초점이 맞춰짐
  , delay: 300  //autocomplete 딜레이 시간(ms)
  , select: function (evt, ui) {
  }
});

document.addEventListener("DOMContentLoaded", function () {
  document.querySelector('body').addEventListener('click', function (event) {
    if (event.target.classList.contains('btn-outline-success')) {
      navigator.geolocation.getCurrentPosition(function (position) {
        var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

        const isbn2 = event.target.getAttribute("data-isbn");
        const isbn = BigInt(Number(isbn2));
        openPopup_MapBook(isbn.toString(), lat, lon);
      });
    }
  });
});

let page = 1;
let isLastPage = false;
let perPage = 10; // 한 페이지당 데이터 개수

$(window).scroll(function () {
  if ($(window).scrollTop() == $(document).height() - $(window).height()) {
    if (!isLastPage) {
      loadMoreData(++page);
    }
  }
});

function loadMoreData(page) {
  let query = $('#search-input').val();

  $.ajax({
    type: 'GET',
    url: `/books/search?query=${query}&page=${page}`,
    success: function (response) {
      let books = response.documents;
      let meta = response.meta;

      // 페이지당 데이터 개수보다 적게 받았다면 마지막 페이지로 판단
      if (meta.totalElements < perPage) {
        isLastPage = true;
      }

      for (let i = 0; i < books.length; i++) {
        let book = books[i];
        let tempHtml = addHTML(book);
        $('#book_container').append(tempHtml);
      }
    },
    error: function (error, status, request) {
      console.error(error);
    }
  });
}

