-- 流程定義表
CREATE TABLE flow_definition (
    flow_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_name VARCHAR(100) NOT NULL,
    flow_description CLOB,
    version VARCHAR(20) NOT NULL, 
    status VARCHAR(20) NOT NULL,
    start_node_id VARCHAR(50),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);
CREATE INDEX idx_status ON flow_definition(status);

-- 決策節點表 
CREATE TABLE decision_node (
    node_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    node_name VARCHAR(100) NOT NULL,
    node_description CLOB,
    decision_type VARCHAR(50) NOT NULL,
    implementation_class VARCHAR(255),
    spel_expression CLOB,
    node_order INT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_decision_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id)
);
CREATE INDEX idx_decision_flow ON decision_node(flow_id);

-- 處理節點表
CREATE TABLE process_node (
    node_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    node_name VARCHAR(100) NOT NULL,
    node_description CLOB,
    process_type VARCHAR(50) NOT NULL,
    implementation_class VARCHAR(255),
    spel_expression CLOB,
    state_name VARCHAR(50),
    node_order INT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_process_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id)
);
CREATE INDEX idx_process_flow ON process_node(flow_id);

-- 節點關聯表
CREATE TABLE node_relation (
    relation_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    source_node_id VARCHAR(50) NOT NULL,
    source_node_type VARCHAR(20) NOT NULL,
    target_node_id VARCHAR(50) NOT NULL,
    target_node_type VARCHAR(20) NOT NULL,
    relation_type VARCHAR(20) NOT NULL,
    logic_operator VARCHAR(10),
    condition_expression CLOB,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_relation_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id)
);
CREATE INDEX idx_relation_source ON node_relation(source_node_id);
CREATE INDEX idx_relation_target ON node_relation(target_node_id);

-- 決策群組表
CREATE TABLE decision_group (
    group_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    group_name VARCHAR(100) NOT NULL,
    logic_operator VARCHAR(10) NOT NULL,
    target_process_id VARCHAR(50),
    parent_group_id VARCHAR(50),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_group_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id),
    CONSTRAINT fk_group_process FOREIGN KEY (target_process_id) REFERENCES process_node(node_id)
);
CREATE INDEX idx_group_flow ON decision_group(flow_id);

-- 決策群組成員表
CREATE TABLE decision_group_member (
    member_id VARCHAR(50) NOT NULL PRIMARY KEY,
    group_id VARCHAR(50) NOT NULL,
    decision_node_id VARCHAR(50) NOT NULL,
    member_order INT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_member_group FOREIGN KEY (group_id) REFERENCES decision_group(group_id),
    CONSTRAINT fk_member_decision FOREIGN KEY (decision_node_id) REFERENCES decision_node(node_id)
);
CREATE INDEX idx_member_group ON decision_group_member(group_id);

-- 執行日誌表
CREATE TABLE execution_log (
    log_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    execution_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    node_id VARCHAR(50),
    node_type VARCHAR(20),
    execution_result VARCHAR(20),
    result_data CLOB,
    error_message CLOB,
    execution_duration_ms INT
);
CREATE INDEX idx_log_flow_customer ON execution_log(flow_id, customer_id);
CREATE INDEX idx_log_execution_time ON execution_log(execution_time);