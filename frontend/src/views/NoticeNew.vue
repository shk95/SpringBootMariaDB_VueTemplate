<template>
    <div>
        <h2>공지사항 등록하기</h2>
        <hr />
        <br />
        <form v-on:submit.prevent="doSubmit">
            <div class="divTable minimalistBlack">
                <div class="divTableBody">
                    <div class="divTableRow">
                        <div class="divTableCell">제목
                        </div>
                        <div class="divTableCell">
                            <input class="form-control" type="text" name="title" v-model="form.title"
                                @blur="v$.form.title.$touch" style="width: 95%;" />
                            <div class="field-error" v-if="v$.form.title.$dirty">
                                <p class="error" v-for="error of v$.form.title.$errors" :key="error.$uid"
                                    v-text="error.$message"></p>
                            </div>
                        </div>
                    </div>
                    <div class="divTableRow">
                        <div class="divTableCell">공지글 여부
                        </div>
                        <div class="divTableCell">
                            예<input type="radio" name="noticeYn" value="Y" v-model="form.noticeYn" />
                            아니오<input type="radio" name="noticeYn" value="N" v-model="form.noticeYn" checked />
                        </div>
                    </div>
                    <div class="divTableRow">
                        <div class="divTableCell">내용
                        </div>
                        <div class="divTableCell">
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
                <button type="submit">등록</button>
                <button type="reset">다시 작성</button>
            </div>
        </form>
    </div>
</template>

<script>
import { useVuelidate } from '@vuelidate/core'
import { required, maxLength, minLength } from '@vuelidate/validators'

export default {
    name: 'NoticeNew',
    setup() {
        return { v$: useVuelidate() }
    },
    data() {
        return {
            form: {
                title: '',
                noticeYn: 'N',
                contents: '',
                userId: ''
            }
        }
    },
    mounted() {
    },
    computed: {
    },
    beforeUnmount() {
    },
    methods: {
        // 공지사항 저장하기
        doSubmit() {
            this.v$.$touch()
            if (this.v$.$invalid) {
                alert('내용을 입력해주세요.')
                return
            }
            this.$axios.post('/notice/', this.form).then(({ data }) => {
                alert(data.msg)
                this.$router.push({ name: 'noticeList' })
            }).catch(error => {
                console.log("error\n\n" + error)
            })
        }
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
    }
}
</script>

<style lang="scss"></style>
