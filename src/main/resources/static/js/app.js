// import { createApp } from "./vue.js";
//
// const vm = createApp({
//     data() {
//         return {
//             yikes: false,
//             msg: "daaaaa",
//             counter: 0
//         }
//     },
//     methods: {
//         doStuff() {
//             this.yikes = !this.yikes;
//         }
//     }
//     // mounted() {
//     //     setInterval(_ => {
//     //         this.counter++
//     //     , 1000})
//     // }
// });
// vm.mount("#app");

// $(".nav-btn").on("click", e => {
//     e.preventDefault();
//     if($(".navbar").hasClass("navbar-open")) {
//         $(".navbar").removeClass("navbar-open");
//     }
//     else {
//         $(".navbar").addClass("navbar-open");
//     }
// });

$.extend( true, $.fn.dataTable.ext.buttons,{
    "btnCopy": {"extend": "copyHtml5", exportOptions: {columns: ':visible'}, "text": "Copiaza", "titleAttr": "Copiaza", "className": "btn btn-primary"},
    "btnExcel": {"extend": "excelHtml5", exportOptions: {columns: ':visible'}, "text": "Excel", "titleAttr": "Excel", "className": "btn btn-primary ml-1"},
    "btnPDF": {"extend": "pdfHtml5", exportOptions: {columns: ':visible'}, "text": "Pdf", "titleAttr": "Pdf", "className": "btn btn-primary ml-1"},
    "btnPrint": {"extend": "print", exportOptions: {columns: ':visible'}, "text": "Imprima", "titleAttr": "Imprima", "className": "btn btn-primary ml-1"}
});

$.fn.datetimepicker.Constructor.Default = $.extend({}, $.fn.datetimepicker.Constructor.Default, {
    icons: {
        time: 'far fa-clock',
        date: 'far fa-calendar',
        up: 'fas fa-arrow-up',
        down: 'fas fa-arrow-down',
        previous: 'fas fa-chevron-left',
        next: 'fas fa-chevron-right',
        today: 'fas fa-calendar-check-o',
        clear: 'fas fa-trash',
        close: 'fas fa-times'
    }
});

console.log("Hello World");