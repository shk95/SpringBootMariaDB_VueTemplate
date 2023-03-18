<template>
    <div>
        <h2>공지사항 상세보기</h2>
        <hr />
        <br />
        <div class="divTable minimalistBlack">
            <div class="divTableBody">
                <div class="divTableRow">
                    <div class="divTableCell">제목
                    </div>
                    <div class="divTableCell" v-text="title"></div>
                </div>
                <div class="divTableRow">
                    <div class="divTableCell">공지글 여부
                    </div>
                    <div class="divTableCell">
                        예<input type="radio" v-if="parseNoticeYn" checked readonly onclick="return false" />
                        <input type="radio" v-if="!parseNoticeYn" readonly onclick="return false" />
                        아니오<input type="radio" v-if="!parseNoticeYn" checked readonly onclick="return false" />
                        <input type="radio" v-if="parseNoticeYn" readonly onclick="return false" />
                    </div>
                </div>
                <div class="divTableRow">
                    <div class="divTableCell">작성일</div>
                    <div class="divTableCell" v-text="regDt"></div>
                </div>
                <div class="divTableRow">
                    <div class="divTableCell">조회수</div>
                    <div class="divTableCell" v-text="readCnt"></div>
                </div>
                <div class="divTableRow">
                    <div class="divTableCell">내용</div>
                    <div class="divTableCell" v-text="contents"></div>
                </div>
            </div>
        </div>
        <div>
            <button type="button" v-on:click="doEdit">수정</button>
            <button type="button" v-on:click="doDelete">삭제</button>
            <button type="button" v-on:click="goBackward">목록</button>
        </div>
    </div>
</template>

<script>
export default {
    name: 'NoticeDetail',
    data() {
        return {
            noticeSeq: '',
            userId: '',
            title: '',
            noticeYn: '',
            readCnt: '',
            contents: '',
            regDt: ''
        }
    },
    mounted() {
        this.loadInitial().then(data => {
            this.noticeSeq = data.noticeSeq
            this.userId = data.userId
            this.title = data.title
            this.noticeYn = data.noticeYn
            this.readCnt = data.readCnt
            this.contents = data.contents
            this.regDt = data.regDt
        }).catch(error => {
            console.error("로딩 실패\n" + error)
        })
    },
    computed: {
        parseNoticeYn() {
            return this.noticeYn === 'Y'
        }
    },
    methods: {
        loadInitial() {
            const noticeSeq = this.$route.params.noticeSeq
            return new Promise((resolve, reject) => {
                this.$axios.get('/notice/' + noticeSeq)
                    .then(({ data }) => resolve(data))
                    .catch((error) => reject(error))
            })
        },
        //수정하기
        doEdit() {
            this.$router.push({ name: 'noticeEdit', params: { noticeSeq: this.noticeSeq } })
        },
        //삭제하기
        doDelete() {
            if (confirm("작성한 글을 삭제하시겠습니까?")) {
                this.$axios.delete('/notice/' + this.noticeSeq).then(({ data }) => {
                    console.log(data)
                    alert("삭제 성공")
                    this.$router.push({ name: 'noticeList' })
                }).catch(error => {
                    console.error("error\n\n" + error)
                })
            }
        },
        goBackward() {
            this.$router.push({ name: 'noticeList' })
        }
    }
}
</script>

<style lang="scss"></style>
