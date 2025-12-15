// 追加フォームオープン
function openAddForm(){
    const form = document.getElementById("addForm");

    // フォームが非表示または初期状態なら表示、表示中なら非表示
    if (form.style.display === "none" || form.style.display === "") {
        form.style.display = "block"; // フォームを表示
    } else {
        form.style.display = "none";  // フォームを非表示
    }
}

// 追加フォームクローズ
function closeAddForm(){
    const form = document.getElementById("addForm");
    form.style.display = "none";  // フォームを非表示
}

// 追加確認画面オープン
function openAddComfirm() {
    const comfirm = document.getElementById("addComfirm");
    if (comfirm.style.display === "none" || comfirm.style.display === "") {
        comfirm.style.display = "block";
    } else {
        comfirm.style.display = "none"; 
    }
}

// 追加フォーム⇒追加確認画面時のアクション
function afterAddForm(event){
    if(event.status === "success") {
        closeAddForm();
        openAddComfirm();
    }
}

// 更新フォームオープン
function openUpdateForm(event) {
    if(event.status === "success") {
        const form = document.getElementById("updateForm");

        // フォームが非表示または初期状態なら表示、表示中なら非表示
        if (form.style.display === "none" || form.style.display === "") {
        form.style.display = "block"; // フォームを表示
        } else {
            form.style.display = "none";  // フォームを非表示
        }
    }
}

// 更新フォームクローズ
function closeUpdateForm(){
    const form = document.getElementById("updateForm");
    form.style.display = "none";  // フォームを非表示
}

// 更新確認画面オープン
function openUpdateComfirm() {
    const comfirm = document.getElementById("updateComfirm");
    if (comfirm.style.display === "none" || comfirm.style.display === "") {
        comfirm.style.display = "block";
    } else {
        comfirm.style.display = "none"; 
    }
}

// 更新フォーム⇒更新確認画面時のアクション
function afterUpdateForm(event) {
    if(event.status === "success") {
        closeUpdateForm();
        openUpdateComfirm();
    }
}

// 削除確認画面オープン
function openDeleteComfirm(event) {
    if(event.status === "success") {
        const comfirm = document.getElementById("deleteComfirm");
        if (comfirm.style.display === "none" || comfirm.style.display === "") {
            comfirm.style.display = "block";
        } else {
            comfirm.style.display = "none"; 
        }
    }
}