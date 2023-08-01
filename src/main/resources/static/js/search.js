
document.querySelector('#search-input-btn').addEventListener('click',
    function (event) {
      const query = $('#search-input').val().trim();
      searchBook(query);
    });

document.querySelector('#search-input').addEventListener('focus',
    function (event) {
      this.value = '';
    });

function addMetaHtml(meta) {
  const query = $('#search-input').val()

  return `<div id="book-box" class="row gx-4 gx-lg-5 align-items-center my-5">
                <div class="col-lg-7">
                    <div id="user-query" data-text = "${query}">[${query}] 에 대한 도서 검색결과  <br>(검색 속도: ${meta.searchTime}초)🕔</div>
                    <p style="color: #636464">[대출 횟수는 1046개 전국 도서관 전체에서 합산된 대출 횟수] </p>  
                </div>
       
            </div>`
}

function addHTML(book) {
  const titleParts = book.title.split(/[:=-]/);
  const mainTitle = titleParts[0];
  const subTitle = titleParts.slice(1).join(":").trim();

  return `<div id="book-box" class="row gx-4 gx-lg-5 align-items-center my-5">
                <div class="col-lg-2">
                  <img id="book-img" class="img-fluid rounded mb-4 mb-lg-0" src="${book.bookImg}" alt="Book image"</>
                </div>
                <div class="col-lg-6">
                    <h3 class="font-weight-light">${mainTitle}</h3>
                    <h5 style="color: #636464">${subTitle}</h5>
                  <p>대출 횟수 : ${book.loanCnt} / ISBN : ${book.isbn}</p>
                  <p>저자 : ${book.author}</p>
                    <p>${book.content}</p>
                </div>
              <div class="col-lg-2">
                <a class="btn btn-outline-success" data-isbn="${book.isbn}" data-title="${book.title}">대출 가능 <br>도서관 찾기!</a>
              </div>
            </div>`
}

function addNotFoundHTML(query) {
  return `<div id="book-box" class="row gx-4 gx-lg-5 align-items-center my-5" style="padding-top: 100pt; padding-bottom: 300pt">
    <!-- default page-->
    <div class="col-lg-2">
      <img id="book-img" class="img-fluid rounded mb-4 mb-lg-0"
           src="https://image.yes24.com/goods/5703744/L"
           alt="Book image">
    </div>
    <div class="col-lg-5">
      <h2 class="font-weight-light"> [${query}] <br>라는 책을 못 찾겠네요...😲😲</h2><br>
      <h4>다음엔 찾을 수 있도록 업데이트 하고 있을게요</h4>
      <p style="color: #636464">(명사를 많이 넣으면 더 잘 찾아요)</p><br>
      <p style="color: #636464">최근 도서 업데이트 : 23년 5월</p>
    </div>
  </div>`
}

function openPopup_MapBook(isbn, lat, lon, title) {
  // 데이터 객체에 isbn, lat, lon,title 값을 추가
  const data = {
    isbn: isbn,
    lat: lat,
    lon: lon,
    title: title
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

const $bookContainer = $('#book_container');

function notFoundBooks(query){
  const message = addNotFoundHTML(query);
  $bookContainer.empty();
  $bookContainer.append(message);
}

function renewMetaData(response){
  const meta = response.meta;
  const tempMetaHtml = addMetaHtml(meta);
  $bookContainer.append(tempMetaHtml);
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

      if (response.meta.totalPages === 0) {
        notFoundBooks(query);
        return;
      }

      $bookContainer.empty();
      renewMetaData(response);

      response.documents.forEach(book => {
        let tempHtml = addHTML(book);
        $('#book_container').append(tempHtml);
      });

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
      success: function (data) {
        const books = data.documents;
        books.unshift({title: ""});
        books.unshift({title: ""});
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
    // 선택한 값으로 검색창의 값을 갱신
    $('#search-input').val(ui.item.label);
    return false;
  },
  close: function (evt) {
    // 검색 함수를 호출
    const query = $('#search-input').val().trim();
    if (query !== '') {
      searchBook(query);
    }
  }
});


let page = 1;
let isLastPage = false;
let perPage = 10; // 한 페이지당 데이터 개수

$(window).scroll(function () {
  if ($(window).scrollTop() === $(document).height() - $(window).height()) {
    if (!isLastPage) {
      loadMoreData(++page);
    }
  }
});

function loadMoreData(page) {
  let query = $('#search-input').val();

  if(query === ''){
    query = $('#user-query').data('text');
  }

  $.ajax({
    type: 'GET',
    url: `/books/search?query=${query}&page=${page}`,
    success: function (response) {
      const books = response.documents;
      const meta = response.meta;

      // 페이지당 데이터 개수보다 적게 받았다면 마지막 페이지로 판단
      if (meta.totalElements < perPage) {
        isLastPage = true;
      }

      books.forEach(book => {
        let tempHtml = addHTML(book);
        $('#book_container').append(tempHtml);
      });

    },
    error: function (error) {
      console.error(error);
    }
  });
}

/* 내 주변 도서관 버튼 관련*/
document.addEventListener("DOMContentLoaded", function () {
  document.querySelector('body').addEventListener('click', function (event) {
    if (event.target.classList.contains('btn-outline-success')) {
      navigator.geolocation.getCurrentPosition(function (position) {
        const lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

        const title = event.target.getAttribute("data-title");
        const isbn = BigInt(Number(event.target.getAttribute("data-isbn")));
        openPopup_MapBook(isbn.toString(), lat, lon, title);
      });
    }
  });
});

document.querySelector('#search-input').addEventListener('keydown',
    function (event) {

      if (event.defaultPrevented) {
        return;
      }

      if (event.keyCode === 13) {
        const query = $('#search-input').val().trim();
        searchBook(query);
      }
    });