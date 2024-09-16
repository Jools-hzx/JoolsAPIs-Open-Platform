// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** listInvokeAnalysis POST /api/interfacesInfo/analysis/invoke/total/list */
export async function listInvokeAnalysisUsingPost(
  body: API.InterfaceAnalysisQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListInterfaceAnalysisVO_>(
    '/api/interfacesInfo/analysis/invoke/total/list',
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    },
  );
}
