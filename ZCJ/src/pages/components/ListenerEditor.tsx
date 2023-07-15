import {
  ProCard,
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormFieldSet,
  ProFormList,
  ProFormListProps,
  ProFormRadio,
  ProFormSegmented,
  ProFormText,
} from "@ant-design/pro-components";
import React from "react";
import Holder from "@/pages/components/Holder";
import SuperProFormSelect from "@/pages/components/SuperProFormSelect";

const ListenerEditor: React.FC<ProFormListProps<any>> = (properties) => {
  const colorfulBorder = () => {
    return `1px solid rgba(${(255 * Math.random()).toFixed(0)}, ${(
      255 * Math.random()
    ).toFixed(0)}, ${(255 * Math.random()).toFixed(0)}, 1)`;
  };

  return (
    <ProFormList
      style={{
        marginBottom: 6,
      }}
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
          creatorRecord={{ arguments: {} }}
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
              execute: "执行",
              input: "输入",
              notify: "通知",
              play: "播放",
              spawn: "生成",
            }}
          />
          {/*  -  */}
          <ProFormDependency name={["action", "arguments"]}>
            {({ action, arguments: options }) => {
              let elements: React.ReactNode = [];
              //
              switch (action) {
                case "delay": {
                  elements = [
                    <ProFormDigit
                      label="延迟时间"
                      fieldProps={{
                        suffix: "Millisecond",
                      }}
                      name="arguments.period"
                      initialValue={options.period}
                    />,
                  ];
                  //
                  break;
                }
                case "execute": {
                  elements = [
                    <ProForm.Item
                      isListField
                      label="命令列表"
                      tooltip="如果只有一条命令, 则该命令必然执行 | 如果有多条命令, 则会随机执行其中一条命令"
                      style={{ marginBlockEnd: 0 }}
                    >
                      <ProFormList
                        min={1}
                        name="arguments.content"
                        initialValue={options.content?.map((item: string) => ({
                          item,
                        }))}
                        itemRender={(
                          { listDom: element, action },
                          { index },
                        ) => (
                          <ProCard
                            bordered
                            hoverable
                            type="inner"
                            collapsible
                            headerBordered
                            extra={action}
                            defaultCollapsed
                            title={`命令 ${index + 1}`}
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
                        <ProFormText name="item" rules={[{ required: true }]} />
                      </ProFormList>
                    </ProForm.Item>,
                  ];
                  //
                  break;
                }
                case "input": {
                  elements = [
                    <ProFormRadio.Group
                      label="类型"
                      radioType="button"
                      name="arguments.type"
                      initialValue={options.type}
                      valueEnum={{
                        keyPress: "按下键盘按键",
                        keyRelease: "释放键盘按键",
                        mousePress: "按下鼠标按键",
                        mouseRelease: "释放鼠标按键",
                        mouseMove: "移动鼠标",
                      }}
                    />,
                    <ProFormDependency name={["arguments.type"]}>
                      {(values) => (
                        <ProForm.Group>
                          {values["arguments.type"] === "mouseMove" ? (
                            <ProFormFieldSet
                              label="偏移"
                              name="arguments.name"
                              initialValue={options.offset}
                            >
                              <ProFormText rules={[{ required: true }]} />
                              <ProFormText rules={[{ required: true }]} />
                            </ProFormFieldSet>
                          ) : (
                            <SuperProFormSelect
                              showSearch
                              width="md"
                              label="子类型"
                              name="arguments.subtype"
                              valueEnum={
                                values["arguments.type"]
                                  ? values["arguments.type"].startsWith("key")
                                    ? Holder.keyEvent
                                    : Holder.inputEvent
                                  : {}
                              }
                              initialValue={options.subtype}
                            />
                          )}
                        </ProForm.Group>
                      )}
                    </ProFormDependency>,
                  ];
                  //
                  break;
                }
                case "notify": {
                  const tip = "Tick (20 Tick = 1 Second)";

                  elements = [
                    <ProFormRadio.Group
                      label="类型"
                      radioType="button"
                      name="arguments.type"
                      initialValue={options.type}
                      valueEnum={{
                        title: "标题",
                      }}
                    />,
                    <ProFormDependency name={["arguments.type"]}>
                      {(values) =>
                        values["arguments.type"] === "title" ? (
                          <ProForm.Group>
                            <ProFormText
                              label="主标题"
                              rules={[{ required: true }]}
                              name="arguments.title"
                              initialValue={options.title}
                            />
                            <ProFormText
                              rules={[{ required: true }]}
                              name="arguments.subtitle"
                              label="副标题"
                              initialValue={options.subtitle}
                            />
                            <ProFormDigit
                              label="渐现时长"
                              fieldProps={{
                                suffix: "Tick",
                              }}
                              rules={[{ required: true }]}
                              name="number:arguments.fadeIn"
                              tooltip={`${tip} | 默认: 10 Tick`}
                              initialValue={options.fadeIn}
                            />
                            <ProFormDigit
                              rules={[{ required: true }]}
                              tooltip={`${tip} | 默认: 70 Tick`}
                              label="持续时长"
                              fieldProps={{
                                suffix: "Tick",
                              }}
                              name="number:arguments.stay"
                              initialValue={options.stay}
                            />
                            <ProFormDigit
                              rules={[{ required: true }]}
                              tooltip={`${tip} | 默认: 20 Tick`}
                              label="渐隐时长"
                              fieldProps={{
                                suffix: "Tick",
                              }}
                              name="number:arguments.fadeOut"
                              initialValue={options.fadeOut}
                            />
                          </ProForm.Group>
                        ) : (
                          <></>
                        )
                      }
                    </ProFormDependency>,
                  ];
                  //
                  break;
                }
                case "play": {
                  elements = [
                    <ProFormRadio.Group
                      label="类型"
                      radioType="button"
                      name="arguments.type"
                      initialValue={options.type}
                      valueEnum={{
                        effect: "效果",
                        sound: "音效",
                        music: {
                          text: "音乐",
                          disabled: true,
                        },
                      }}
                    />,
                    <ProFormDependency name={["arguments.type"]}>
                      {(values) =>
                        values["arguments.type"] === "music" ? (
                          <ProFormText
                            rules={[{ required: true }]}
                            label="音频文件所在的位置"
                            name="arguments.location"
                            initialValue={options.location}
                          />
                        ) : (
                          <ProForm.Group>
                            <SuperProFormSelect
                              showSearch
                              width="md"
                              label="子类型"
                              name="arguments.subtype"
                              valueEnum={
                                values["arguments.type"]
                                  ? // @ts-ignore
                                    Holder[values["arguments.type"]]
                                  : {}
                              }
                              initialValue={options.subtype}
                            />
                            {values["arguments.type"] === "sound" && (
                              <ProForm.Group>
                                <ProFormDigit
                                  max={1}
                                  label="音高"
                                  tooltip="默认: 0F"
                                  name="number:arguments.pitch"
                                  initialValue={options.pitch}
                                />
                                <ProFormDigit
                                  max={1}
                                  label="音量"
                                  tooltip="默认: 1F"
                                  name="number:arguments.volume"
                                  initialValue={options.volume}
                                />
                              </ProForm.Group>
                            )}
                          </ProForm.Group>
                        )
                      }
                    </ProFormDependency>,
                  ];
                  //
                  break;
                }
                case "spawn": {
                  elements = [
                    <ProFormRadio.Group
                      label="类型"
                      radioType="button"
                      name="arguments.type"
                      initialValue={options.type}
                      valueEnum={{
                        entity: "实体",
                        practice: "粒子",
                      }}
                    />,
                    <ProFormDependency name={["arguments.type"]}>
                      {(values) => (
                        <ProForm.Group>
                          <SuperProFormSelect
                            showSearch
                            width="md"
                            label="子类型"
                            name="arguments.subtype"
                            valueEnum={
                              values["arguments.type"]
                                ? // @ts-ignore
                                  Holder[values["arguments.type"]]
                                : {}
                            }
                            initialValue={options.subtype}
                          />
                          {values["arguments.type"] === "entity" && (
                            <ProFormText
                              label="实体名称"
                              name="arguments.name"
                              initialValue={options.name}
                            />
                          )}
                        </ProForm.Group>
                      )}
                    </ProFormDependency>,
                    <ProFormDigit
                      label="生成数量"
                      name="number:arguments.amount"
                      initialValue={options.amount}
                    />,
                  ];
                  //
                  break;
                }
              }
              //
              return action === "execute" ? (
                <>
                  <ProForm.Group
                    title="参数"
                    titleStyle={{
                      marginBlockEnd: 12,
                    }}
                  />
                  {elements}
                </>
              ) : (
                <ProForm.Group
                  title="参数"
                  titleStyle={{
                    marginBlockEnd: 12,
                  }}
                >
                  {elements}
                </ProForm.Group>
              );
            }}
          </ProFormDependency>
        </ProFormList>
      </ProForm.Item>
    </ProFormList>
  );
};

export default ListenerEditor;
