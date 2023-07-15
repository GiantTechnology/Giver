import {
  ProCard,
  ProForm,
  ProFormFieldSet,
  ProFormList,
  ProFormListProps,
  ProFormRadio,
  ProFormSegmented,
  ProFormText,
} from "@ant-design/pro-components";
import React from "react";

const ListenerEditor: React.FC<ProFormListProps<any>> = (properties) => {
  const colorfulBorder = () => {
    return `1px solid rgba(${(255 * Math.random()).toFixed(0)}, ${(
      255 * Math.random()
    ).toFixed(0)}, ${(255 * Math.random()).toFixed(0)}, 1)`;
  };

  return (
    <ProFormList
      itemRender={({ listDom: element, action }, { index }) => (
        <ProCard
          bordered
          hoverable
          type="inner"
          collapsible
          headerBordered
          extra={action}
          defaultCollapsed
          title={`监听器 ${index + 1}`}
          style={{
            marginBlockEnd: 8,
            border: colorfulBorder(),
          }}
          bodyStyle={{ paddingBlockEnd: 0 }}
        >
          {element}
        </ProCard>
      )}
      {...properties}
    >
      <ProFormSegmented
        name="type"
        label="类型"
        valueEnum={{
          gift: "礼物",
          chat: "聊天",
          follow: "关注",
          share: "分享",
          like: "点赞",
          enter: "用户进入房间",
          broadcast: "直播间人数广播",
        }}
      />
      <ProFormText
        name="condition"
        label="触发条件"
        tooltip="Inline Javascript"
      />
      {/*  -  */}
      <ProForm.Item isListField label="动作">
        <ProFormList
          name="actions"
          itemRender={({ listDom: element, action }, { index }) => (
            <ProCard
              bordered
              hoverable
              type="inner"
              collapsible
              headerBordered
              extra={action}
              defaultCollapsed
              title={`动作 ${index + 1}`}
              style={{
                marginBlockEnd: 8,
                border: colorfulBorder(),
              }}
              bodyStyle={{ paddingBlockEnd: 0 }}
            >
              {element}
            </ProCard>
          )}
        >
          {/*  -  */}
          <ProFormRadio.Group
            name="action"
            label="类型"
            radioType="button"
            valueEnum={{
              delay: "延迟",
              input: "输入",
              play: "播放",
              spawn: "生成",
            }}
          />
          {/*  -  */}
          {/*  Todo  */}
          <ProFormFieldSet label="参数" name="arguments">
            <ProFormText name="subtype" label="子类型" />
            <ProFormText name="name" label="名称" />
          </ProFormFieldSet>
        </ProFormList>
      </ProForm.Item>
    </ProFormList>
  );
};

export default ListenerEditor;
