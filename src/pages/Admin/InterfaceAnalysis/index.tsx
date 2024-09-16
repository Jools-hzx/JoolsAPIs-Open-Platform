import '@umijs/max';
import React, {useEffect, useState} from 'react';
import ReactECharts from 'echarts-for-react';
import {PageContainer} from "@ant-design/pro-components";
import {listInvokeAnalysisUsingPost} from "@/services/Hzx-Open-Apis-Platform/interfaceAnalysisController";  // or var ReactECharts = require('echarts-for-react');

const InterfaceAnalysis: React.FC = () => {

    const [data, setData] = useState<APl.InterfaceInfoV0[]>([])
    useEffect(() => {
      // 定义 async 函数来发送请求
      const fetchData = async () => {
        try {
          const res = await listInvokeAnalysisUsingPost({
            limit: 5
          });  // 将参数以对象形式传递
          if (res.data) {
            setData(res.data);  // 设置响应数据
          }
        } catch (e: any) {
          console.error("Error fetching data", e);  // 错误处理
        }
      };
      fetchData();  // 调用 async 函数
    }, []);

    //映射:{value:1048,name:'Search Engine'}
    const chartData = data.map(item => {
      return {
        value: item.totalNum,
        name: item.name
      }
    })

    // 通过 const 声明 option 变量
    const option = {
      legend: {
        top: 'top'
      },
      toolbox: {
        show: true,
        feature: {
          mark: {show: true},
          dataView: {show: true, readOnly: false},
          restore: {show: true},
          saveAsImage: {show: true}
        }
      },
      series: [
        {
          name: '调用次数最多的接口 TOP 5',
          type: 'pie',
          radius: [40, 220],
          center: ['50%', '50%'],
          roseType: 'area',
          itemStyle: {
            borderRadius: 6
          },
          data: chartData
        }
      ]
    };

    // 定义 loading 状态
    const [loading, setLoading] = useState(false);

    return (
      <PageContainer>
        <ReactECharts
          style={{width: '100%', height: '500px'}}  // 设置图表容器的宽度和高度
          loadingOption={{
            showLoading: loading
          }}
          option={option}
        />
      </PageContainer>
    );
  }
;

export default InterfaceAnalysis;
