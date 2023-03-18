import { createRouter, createWebHistory } from "vue-router";
import NoticeDetail from "@/views/NoticeDetail";
import NoticeEdit from "@/views/NoticeEdit";
import NoticeList from "@/views/NoticeList";
import NoticeNew from "@/views/NoticeNew";

const routes = [
  {
    name: "noticeDetail",
    path: "/notice/:noticeSeq",
    component: NoticeDetail,
  },
  {
    name: "noticeEdit",
    path: "/notice/:noticeSeq/edit",
    component: NoticeEdit,
  },
  {
    name: "noticeList",
    path: "/notice",
    component: NoticeList,
  },
  {
    name: "noticeNew",
    path: "/notice/new",
    component: NoticeNew,
  },
];

const router = createRouter({
  history: createWebHistory(/* process.env.BASE_URL */),
  routes,
});

export default router;
