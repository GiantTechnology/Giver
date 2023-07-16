import {
  ProCard,
  ProCardProps,
  ProForm,
  ProFormDigit,
  ProFormList,
  ProFormText,
} from "@ant-design/pro-components";
import React, { Dispatch, MutableRefObject, SetStateAction } from "react";
import ListenerEditor from "@/pages/components/ListenerEditor";
import { SaveOutlined } from "@ant-design/icons";

const Editor: React.FC<
  ProCardProps & {
    context: [MutableRefObject<Record<string, any> | undefined>];
  }
> & {
  isProCard: boolean;
} = (properties) => {
  const [content] = properties.context;

  return (
    <ProCard direction="column" ghost gutter={[6, 6]} {...properties}>
      {/*  -  */}
      <ProCard ghost direction="row" gutter={[6, 6]}>
        {/*  -  */}
        <ProCard
          title="解码器"
          colSpan={{
            xs: 24,
            md: 12,
          }}
          bordered
          boxShadow
          headerBordered
        >
          <ProForm
            submitter={false}
            onChange={(event: any) => {
              //
              let key = event.target.id;
              let value = event.target.className.includes(
                "ant-input-number-input",
              )
                ? Number(event.target.value)
                : event.target.value;
              //
              if (event.target.id.startsWith("bindings")) {
                // 处理从 Input 获取来的原始 Binding 数据
                const chips = key.split("_");
                const bindings =
                  content?.current?.listeners?.interaction?.bindings;

                if (bindings?.length > chips[1]) {
                  bindings[chips[1]][chips[2]] = value;
                } else {
                  bindings.push({
                    [chips[2]]: value,
                  });
                }
                // 重设 Event Target Identifier 用于后续处理
                key = "bindings";
                value = bindings;
              }
              //
              if (content?.current?.listeners?.interaction) {
                content.current.listeners.interaction[key] = value;
              }
            }}
            initialValues={content?.current?.listeners?.interaction}
          >
            <ProFormText name="fqdn" label="地址" />
            <ProFormList
              style={{
                marginBottom: 6,
              }}
              name="bindings"
              label="绑定"
              itemRender={({ listDom: element, action }, { index }) => (
                <ProCard
                  bordered
                  hoverable
                  type="inner"
                  collapsible
                  headerBordered
                  extra={action}
                  defaultCollapsed
                  title={`绑定 ${index + 1}`}
                  style={{
                    marginBlockEnd: 8,
                  }}
                  bodyStyle={{ paddingBlockEnd: 0 }}
                >
                  {element}
                </ProCard>
              )}
              {...properties}
            >
              <ProFormDigit name="room" label="房间号" />
              <ProFormText name="player" label="玩家名" />
            </ProFormList>
          </ProForm>
        </ProCard>
        {/*  -  */}
        <ProCard
          title="环境"
          colSpan={{
            xs: 24,
            md: 12,
          }}
          bordered
          boxShadow
          headerBordered
        >
          <ProForm
            submitter={false}
            onChange={(event: any) => {
              if (content?.current?.entities?.environment) {
                content.current.entities.environment[event.target.id] =
                  event.target.className.includes("ant-input-number-input")
                    ? Number(event.target.value)
                    : event.target.value;
              }
            }}
            initialValues={content?.current?.entities?.environment}
          >
            <ProFormDigit name="difficulty" label="难度" />
            <ProFormDigit name="magnification" label="倍率" />
          </ProForm>
        </ProCard>
      </ProCard>
      {/*  -  */}
      <ProCard title="事件监听器" bordered boxShadow headerBordered>
        <ProForm
          submitter={{
            resetButtonProps: false,
            searchConfig: {
              submitText: "序列化数据",
            },
            submitButtonProps: {
              block: true,
              danger: true,
              type: "dashed",
              icon: <SaveOutlined />,
            },
          }}
          onFinish={(formData) => {
            // 数据清洗
            for (let listener of formData.on) {
              for (let listenerKey in listener) {
                switch (listenerKey) {
                  case "undefined": {
                    delete listener[listenerKey];
                    break;
                  }
                  case "actions": {
                    for (let action of listener[listenerKey]) {
                      // 格式化 execute.action
                      if (action["action"] === "execute") {
                        if (action["arguments.content"]) {
                          action["arguments.content"] = action[
                            "arguments.content"
                          ].map((element: any) => element.item);
                        }
                      }
                      //
                      for (let actionKey in action) {
                        // 判断此 key 是否为 number:arguments.${key} 的形式
                        if (actionKey.startsWith("numbers:")) {
                          action.arguments[actionKey] = Number(
                            action[actionKey],
                          );
                        }
                        // 判断此 key 是否为 arguments.${key} 的形式
                        const at = actionKey.indexOf(".");

                        if (at !== -1) {
                          // 如果是, 则将该 key 对应的值赋值给 arguments[${key}]
                          action.arguments[actionKey.substring(at + 1)] =
                            action[actionKey];
                          // 并且从原 action 中删除此 key
                          delete action[actionKey];
                        }
                      }
                    }
                  }
                }
              }
            }
            // 置入数据
            if (content?.current?.listeners?.interaction) {
              content.current.listeners.interaction.on = formData.on;
            }
            //
            return Promise.resolve(true);
          }}
          initialValues={content?.current?.listeners?.interaction}
        >
          <ListenerEditor name="on" />
        </ProForm>
      </ProCard>
    </ProCard>
  );
};

Editor.isProCard = true;

export default Editor;
