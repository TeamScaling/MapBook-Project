document.querySelector('#search-input').addEventListener('keydown',
    function (event) {
      if (event.keyCode === 13) {
        let query = $('#search-input').val().trim();
        searchBook(query);
      }
    });

document.querySelector('#search-input-btn').addEventListener('click',
    function (event) {
      let query = $('#search-input').val().trim();
      searchBook(query);
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
//
// function openPopup_MapBook(isbn, lat, lon) {
//   // 데이터 객체에 isbn, lat, lon 값을 추가
//   const data = {
//     isbn: isbn,
//     lat: lat,
//     lon: lon
//   };
//
//   $.ajax({
//     type: 'POST',
//     url: `/books/mapBook/search`,
//     contentType: "application/json",
//     data: JSON.stringify(data), // 수정된 data 객체를 JSON 형태로 변환하여 전송
//     success: function (response) {
//       // 새로운 팝업 창을 열고 응답을 받은 HTML로 내용을 채움
//       const popupWindow = window.open('', 'areaCdInfo',
//           'width=1200,height=800');
//       popupWindow.document.write(response);
//       popupWindow.document.close();
//     },
//     error(error) {
//       console.error(error);
//     }
//   });
//
//   return false;
// }

function openPopup_MapBook(isbn, lat, lon) {
  // 데이터 객체에 isbn, lat, lon 값을 추가
  const data = {
    isbn: isbn,
    lat: lat,
    lon: lon
  };

  // 팝업 창을 엽니다.
  const popupWindow = window.open('', 'areaCdInfo', 'width=1200,height=800');

  // 먼저 로딩 페이지를 불러옵니다.
  $.get("/books/mapBook/loading", function (loadingPage) {
    // 로딩 페이지가 돌아오면 팝업 창에 렌더링합니다.
    popupWindow.document.write(loadingPage);
    popupWindow.document.close();

    // 이제 본래의 요청을 수행합니다.
    $.ajax({
      type: 'POST',
      url: `/books/mapBook/search`,
      contentType: "application/json",
      data: JSON.stringify(data), // 수정된 data 객체를 JSON 형태로 변환하여 전송
      success: function (response) {
        // 응답이 돌아오면 팝업 창의 내용을 응답으로 대체합니다.
        popupWindow.document.open();
        popupWindow.document.write(response);
        popupWindow.document.close();
      },
      error(error) {
        // 에러가 발생하면 팝업 창에 에러 메시지를 표시합니다.
        popupWindow.document.open();
        popupWindow.document.write("<p>An error occurred: " + error + "</p>");
        popupWindow.document.close();
      }
    });
  });

  return false;
}



function searchBook(query) {

  if (query === '' || query.length < 2) {
    alert('공백이나 1글자는 못 찾아요😅😅');
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
        alert(error.responseJSON.message);
        $('#search-input').focus();
      } else {
        console.error(error);
      }
    }
  })
}

$('#search-input').autocomplete({
  source: function (request, response) {
    $.ajax({
      url: "/books/autocomplete",
      type: "POST",
      dataType: "JSON",
      data: {query: request.term}, // 검색 키워드
      success: function (books) {
        response(
            $.map(books, function (book) {
              return {
                label: book.title   // 목록에 표시되는 값
              };
            })
        );
      },
      error: function () {
        console.log("오류가 발생했습니다.");
      }
    });
  },
  focus: function (event, ui) {
    return false;
  },
  minLength: 2,
  autoFocus: false,
  delay: 300,
  select: function (evt, ui) {
    // 선택한 값으로 검색창의 값을 갱신하고 검색 함수를 호출
    $('#search-input').val(ui.item.label);
    searchBook(ui.item.label);
    return false;
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

