// 検索用モーダル
// モーダルオープン
function openSearchModal() {
    document.getElementById("searchModal").style.display = "block"
}
//モーダルクローズ
function closeSearchModal() {
    document.getElementById("searchModal").style.display = "none"
}

// 更新用モーダル
// モーダルオープン
function openUpdateModal() {
    document.getElementById("updateModal").style.display = "block"
}
//モーダルクローズ
function closeUpdateModal() {
    document.getElementById("updateModal").style.display = "none"
}

// 削除用モーダル
// オープンモーダル
function openSearchForDelete() {
    document.getElementById("searchForDelete").style.display = "block"
}
// クローズモーダル
function closeSearchForDelete() {
    document.getElementById("searchForDelete").style.display = "none"
}

function openDeleteModal() {
    document.getElementById("deleteModal").style.display = "block"
}
// クローズモーダル
function closeDeleteModal() {
    document.getElementById("deleteModal").style.display = "none"
}

// 追加用モーダル
// オープンモーダル
function openAddModal() {
    document.getElementById("addModal").style.display = "block"
}
// クローズモーダル
function closeAddModal() {
    document.getElementById("addModal").style.display = "none"
}

// id検索フォーム実行後（f:ajaxタグから通知を受け取る）
// 引数にてAjaxイベント受信
function afterSearch(event) {
    // Ajaxイベントがsuccessの時のみ処理
    if(event.status === "success") {
        // updateFormのexistsFlagを取得（Ajaxで更新された後の値）
        var existsFlag = document.getElementById("updateForm:existsFlag").value;

        // document.getElementById("updateForm:existsFlag").valueは文字列を返すため、trueも文字列として比較する。
        if(existsFlag === "true") {
            closeSearchModal();
            openUpdateModal()
        } else {
            closeSearchModal();
            alert("検索対象が見つかりません。");
        }
    }
}


function afterSearchForDelete(event) {
    // Ajaxイベントがsuccessの時のみ処理
    if(event.status === "success") {
        // updateFormのexistsFlagを取得（Ajaxで更新された後の値）
        var existsFlag = document.getElementById("deleteForm:existsFlag").value;

        // document.getElementById("updateForm:existsFlag").valueは文字列を返すため、trueも文字列として比較する。
        if(existsFlag === "true") {
            closeSearchForDelete();
            openDeleteModal()
        } else {
            closeSearchForDelete();
            alert("検索対象が見つかりません。");
        }
    }
}