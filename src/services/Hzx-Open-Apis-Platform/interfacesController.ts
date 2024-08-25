// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addInterfacesInfo POST /api/interfacesInfo/add */
export async function addInterfacesInfoUsingPost(
  body: API.InterfacesInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/interfacesInfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteInterfacesInfo POST /api/interfacesInfo/delete */
export async function deleteInterfacesInfoUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfacesInfo/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getInterfacesInfoVOById GET /api/interfacesInfo/get/vo */
export async function getInterfacesInfoVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfacesInfoVOByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfacesInfoVO_>('/api/interfacesInfo/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listInterfacesInfoByPage POST /api/interfacesInfo/list/page */
export async function listInterfacesInfoByPageUsingPost(
  body: API.InterfacesInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfacesInfo_>('/api/interfacesInfo/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateInterfacesInfo POST /api/interfacesInfo/update */
export async function updateInterfacesInfoUsingPost(
  body: API.InterfacesInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfacesInfo/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** offlineInterfacesInfo POST /api/interfacesInfo/update/status/offline */
export async function offlineInterfacesInfoUsingPost(
  body: API.InterfacesInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfacesInfo/update/status/offline', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** onlineInterfacesInfo POST /api/interfacesInfo/update/status/online */
export async function onlineInterfacesInfoUsingPost(
  body: API.InterfacesInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfacesInfo/update/status/online', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
