import {PageContainer} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {List, message} from 'antd';
import {listInterfacesInfoByPageUsingPost} from "@/services/Hzx-Open-Apis-Platform/interfacesController";

const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.InterfacesInfo[]>([]);
  const [total, setTotal] = useState<number>(0);

  const loadData = async (current = 1, pageSize = 5) => {
    //开始加载数据，设置 loading 状态为 true
    setLoading(true);
    try {
      const res = await listInterfacesInfoByPageUsingPost(
        {
          current,
          pageSize
        });
      // 将请求返回的数据设置到列表数据状态中
      setList(res?.data?.records ?? []);
      setTotal(res?.data?.total ?? 0);
    } catch (error: any) {
      //请求失败的时候提示错误信息
      message.error("请求失败" + error.message);
    }
    // 数据
    setLoading(false)
  }

  useEffect(() => {
    //页面加载完成之后调用加载数据的函数
    loadData();
  }, []);

  return (
    <PageContainer title="Jools 在线接口开发平台">
      <List
        className="my-list"
        // 设置 loading 属性，表示数据是否正在加载中....
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => {
          //构建列表项的链接地址
          const apiLink = `/interface_info/${item.id}`;
          return (<List.Item actions={[<a key={item.id} href={apiLink}> 查看</a>]}>
              <List.Item.Meta
                // href 等会要改成接口文档的链接
                title={<a href={apiLink}>{item.name}</a>}
                description={item.description}
              />
            </List.Item>
          );
        }}
        /*分页配置*/
        pagination={{
          showTotal(total: number) {
            return "总数" + total;
          },
          // 每页显示的条数
          pageSize: 5,
          // 总数,从状态中获取
          total,
          // 切换页面出发的回调函数
          onChange(page, pageSize) {
            // 加载对应页面的数据
            loadData(page, pageSize);
          },
        }}
      />
    </PageContainer>
  );
};

export default Index;
