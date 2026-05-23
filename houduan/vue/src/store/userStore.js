import { defineStore } from "pinia";
import { ref } from "vue";
//登录用户信息
export const useProvinceNameStore = defineStore("provinceName", {
  state: () => ({
    provinceName: "中国",
    mapData: [],
    lineData: [],
    pieAgeData: [],
    pieGenderData: [],
    budgetData: [],
    nowProvinceNum:0
  }),
  actions: {
    setProvinceName(provinceName) {
      this.provinceName=provinceName
    },
    setMapData(mapData) {
      this.mapData=mapData
    },
    setLineData(lineData) {
      this.lineData=lineData
    },
    setPieAgeData(pieAgeData) {
      this.pieAgeData=pieAgeData
    },
    setPieGenderData(pieGenderData) {
      this.pieGenderData=pieGenderData
    },
    setBudgetData(budgetDataData) {
      this.budgetData=budgetDataData
    },
    setProvinceNum(nowProvinceNum) {
      this.nowProvinceNum=nowProvinceNum
    },

  },
});
