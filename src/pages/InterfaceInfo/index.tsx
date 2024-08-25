import {PageContainer} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {useParams} from "react-router";
import {message} from 'antd';
import {Card, Descriptions} from "antd";
import {getInterfacesInfoVoByIdUsingGet} from "@/services/Hzx-Open-Apis-Platform/interfacesController";
// import {listInterfacesInfoByPageUsingPost} from "@/services/Hzx-Open-Apis-Platform/interfacesController";

const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.InterfacesInfo[]>([]);
  // const [total, setTotal] = useState<number>(0);
  //使用 useMatch 钩子函数将当前 URL 与指定的路径模式/interface_info/:id 进行匹配
  //将匹配到的结果赋值给 match 变量
  // const match = useMatch('/interface_info/:id');
  // alert(JSON.stringify(match));
  const params = useParams();

  const loadData = async () => {
    //开始加载数据，设置 loading 状态为 true
    if (!params.id) {
      message.error('参数不存在');
      return;
    }
    setLoading(true);
    try {
      const res = await getInterfacesInfoVoByIdUsingGet({
        id: Number(params.id),
      });
      // 将请求返回的数据设置到列表数据状态中
      setData(res.data);
    } catch (error: any) {
      //请求失败处理
      message.error("请求失败" + error.message);
    }
    // 请求完成，设置 loading 状态为 false, 表示请求结束，可以停止加载状态的显示
    setLoading(false)
  }

  useEffect(() => {
    //页面加载完成之后调用加载数据的函数
    loadData();
  }, []);

  return (
    <PageContainer title="Jools 在线接口开发平台">
      <Card>
        {data ? (
          <Descriptions title={data.name} column={2}>
            <Descriptions.Item label="接口状态">{data.status ? '开启' : '关闭'}</Descriptions.Item>
            <Descriptions.Item label="描述">{data.description}</Descriptions.Item>
            <Descriptions.Item label="请求地址">{data.url}</Descriptions.Item>
            <Descriptions.Item label="请求方法">{data.method}</Descriptions.Item>
            <Descriptions.Item label="请求头">{data.requestHeader}</Descriptions.Item>
            <Descriptions.Item label="响应头">{data.responseHeader}</Descriptions.Item>
            <Descriptions.Item label="创建时间">{data.createTime}</Descriptions.Item>
            <Descriptions.Item label="更新时间">{data.updateTime}</Descriptions.Item>
          </Descriptions>
        ) : (
          <>接口不存在</>
        )}
      </Card>
    </PageContainer>
  );
};

export default Index;
