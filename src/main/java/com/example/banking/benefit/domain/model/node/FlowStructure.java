package com.example.banking.benefit.domain.model.node;

import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.model.relation.NodeRelation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlowStructure {

    private final Map<String, Node> nodeMap;
    private final Map<String, List<NodeRelation>> relationsMap;

    public FlowStructure(List<DecisionNode> decisionNodes, List<ProcessNode> processNodes, List<NodeRelation> relations) {
        this.nodeMap = Stream.concat(decisionNodes.stream(), processNodes.stream())
                .collect(Collectors.toMap(Node::getNodeId, Function.identity()));
        this.relationsMap = relations.stream()
                .collect(Collectors.groupingBy(NodeRelation::getSourceNodeId));
    }

    public Optional<Node> findNodeById(String nodeId) {
        return Optional.ofNullable(nodeMap.get(nodeId));
    }

    public Optional<Node> findNextNode(String sourceNodeId, boolean condition) {
        return Optional.ofNullable(relationsMap.get(sourceNodeId))
                .flatMap(relations -> relations.stream()
                        .filter(r -> r.matchesCondition(condition))
                        .findFirst()
                        .map(NodeRelation::getTargetNodeId)
                        .flatMap(this::findNodeById));
    }
}
