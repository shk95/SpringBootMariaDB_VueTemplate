<template>
    <div>
        <h2>공지사항 수정하기</h2>
        <hr />
        <br />
        <form v-on:submit.prevent="submitForm">
            <input type="hidden" v-model="form.noticeSeq" />
            <div class="divTable minimalistBlack">
                <div class="divTableBody">
                    <div class="divTableRow">
                        <div class="divTableCell">제목</div>
                        <div class="divTableCell form-group">
                            <input class="form-control" type="text" name="title" v-model="form.title"
                                @blur="v$.form.title.$touch" style="width: 95%;" />
                            <div class="field-error" v-if="v$.form.title.$dirty">
                                <p class="error" v-for="error of v$.form.title.$errors" :key="error.$uid"
                                    v-text="error.$message"></p>
                            </div>
                        </div>
                    </div>
                    <div class="divTableRow">
                        <div class="divTableCell">공지글 여부</div>
                        <div class="divTableCell">
                            <input type="radio" name="noticeYn" value="Y" v-model="form.noticeYn" />
                            <label for="noticeY">예</label>
                            <input type="radio" name="noticeYn" value="N" v-model="form.noticeYn" />
                            <label for="noticeN">아니오</label>
                        </div>
                    </div>
                    <div class="divTableRow">
                        <div class="divTableCell">조회수</div>
                        <div class="divTableCell">{{ form.readCnt }}
                        </div>
                    </div>
                    <div class="divTableRow">
                        <div class="divTableCell">내용</div>
                        <div class="divTableCell form-group">
                            <textarea class="form-control" style="width: 95%; height: 400px" name="contents"
                                v-model="form.contents" @blur="v$.form.contents.$touch"></textarea>
                            <div class="field-error" v-if="v$.form.contents.$dirty">
                                <p class="error" v-for="error of v$.form.contents.$errors" :key="error.$uid"
                                    v-text="error.$message"></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <button type="submit">수정</button>
                <button type="reset">지우기</button>
            </div>
        </form>
    </div>
</template>

<script>
import { required, maxLength, minLength } from '@vuelidate/validators'
import { useVuelidate } from '@vuelidate/core'

export default {
    name: 'NoticeEdit',
    setup() {
        return { v$: useVuelidate() }
    },
    mounted() {
        this.loadInitial().then(data => {
            this.form.noticeSeq = data.noticeSeq
            this.form.title = data.title
            this.form.noticeYn = data.noticeYn
            this.form.readCnt = data.readCnt
            this.form.contents = data.contents
        }).catch(error => {
            console.error("로딩 실패\n" + error)
        })
    },
    data() {
        return {
            form: {
                noticeSeq: '',
                title: '',
                noticeYn: '',
                readCnt: '',
                contents: '',
            }
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
        // 공지사항 저장하기
        async submitForm() {
            // this.v$.$touch()
            if (!(await this.v$.$validate())) {
                alert('내용을 입력해주세요.')
                return
            }
            this.$axios.put('/notice/' + this.form.noticeSeq, this.form)
                .then(() => {
                    console.log('ok\n' + this.form)
                    alert('수정되었습니다.')
                    this.$router.push({ name: "noticeList" })
                }).catch((error) => {
                    alert('에러발생.')
                    console.log(error)
                })
        }
    },
    computed: {
    },
    validations() {
        return {
            form: {
                title: {
                    required,
                    minLength: minLength(1),
                    maxLength: maxLength(50)
                },
                contents: {
                    required,
                    minLength: minLength(1),
                    maxLength: maxLength(4000)
                }
            }
        }
    },
    components: {
    }
}
</script>

<style lang="scss"></style>
