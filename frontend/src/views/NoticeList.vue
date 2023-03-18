<template>
    <div>
        <h2>공지사항</h2>
        <hr />
        <br />
        <div class="divTable minimalistBlack">
            <div class="divTableHeading">
                <div class="divTableRow">
                    <div class="divTableHead">순번</div>
                    <div class="divTableHead">제목</div>
                    <div class="divTableHead">조회수</div>
                    <div class="divTableHead">등록자</div>
                    <div class="divTableHead">등록일</div>
                </div>
            </div>
            <div class="divTableBody">
                <div class="divTableRow" v-for="data in dataList" v-bind:key="data.noticeSeq"
                    @click="goDetail(data.noticeSeq)">
                    <div class="divTableCell" v-if="data.noticeYn === 'Y'">{{ data.noticeSeq }}(공지사항)</div>
                    <div class="divTableCell" v-if="!(data.noticeYn === 'Y')">{{ data.noticeSeq }}</div>
                    <div class="divTableCell">{{ data.title }}</div>
                    <div class="divTableCell">{{ data.readCnt }}</div>
                    <div class="divTableCell">{{ data.userId }}</div>
                    <div class="divTableCell">{{ data.regDt }}</div>
                </div>
            </div>
        </div>
        <a href="/notice/new">글쓰기</a>
    </div>
</template>

<script>
export default {
    name: 'NoticeList',
    data() {
        return {
            dataList: [/* listed noticeDTO */]
        }
    },
    mounted() {
        this.loadInitial().then((data) => {
            this.dataList = data
            console.log(this.dataList)
        }).catch(error => {
            console.error("로딩 실패\n" + error)
            alert("로딩 실패\n" + error)
        })
    },
    methods: {
        loadInitial() {
            return new Promise((resolve, reject) => {
                this.$axios.get('/notice/list-all')
                    .then(({ data }) => { resolve(data) })
                    .catch((error) => { reject(error) })
            })
        },
        //상세보기 이동
        goDetail(noticeSeq) {
            this.$router.push({ name: 'noticeDetail', params: { noticeSeq: noticeSeq } })
        },
    }
}
</script>

<style lang = "scss" ></style >