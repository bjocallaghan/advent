#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define VERBOSE 0

// declare a node struct; value, ptr to next
typedef struct Node{
  int value;
  struct Node* next;
} Node;

void dump_nodes(Node* current) {
  Node* n = current;
  int i = 0;
  do {
    printf("%i ", n->value);
    n = n->next;
    } while (n != current && (i++ < 20));
  if (i >= 20) {
    printf("...");
  }
  printf("\n");
}

Node* find(int needle, Node* nodes_by_label[]) {
  return nodes_by_label[needle - 1];
}

Node* traverse(Node* n, int i) {
  while (i-- > 0) {
    n = n->next;
  }
  return n;
}

Node* move(Node* current, int max_value, Node* nodes_by_label[]) {
  // assume the first node is always the current
  if (VERBOSE) {
    printf("cups: ");
    dump_nodes(current);
  }

  // get a handle to the second node (things about to be removed from the circle)
  Node* snip_head = traverse(current, 1);
  // get a handle to the fourth node
  Node* snip_tail = traverse(current, 3);

  // working from current, find the three next values. save them to regular
  // variables. though inelegant, this is done for efficiency later when
  // calculating the destination
  int r1 = current->next->value;
  int r2 = current->next->next->value;
  int r3 = current->next->next->next->value;

  if (VERBOSE) {
    printf("pick up: %i %i %i\n", r1, r2, r3);
  }

  /// calculate the destination (while loop)
  int dest_val = current->value - 1;
  while (dest_val == r1 || dest_val == r2 || dest_val == r3 || dest_val <= 0) {
    dest_val--;
    if (dest_val <= 0) {
      dest_val = max_value;
    }
  }  
  // iterate through until you find the destination
  Node* dest_node = find(dest_val, nodes_by_label);
  if (VERBOSE) {
    printf("destination: %i (intended) %i (actual)\n\n", dest_val, dest_node->value);
  }

  // save the destination node's target for later (dest_target)
  Node* dest_node_orig_target = dest_node->next;

  // rotate current NOW, before things get screwy
  Node* rtn = traverse(current, 4);

  // re-target the current from the second to the fifth (i.e. perform the snip)
  Node* body_right = traverse(current, 4);
  current->next = body_right;

  // point the destination node toward the snip head
  dest_node->next = snip_head;
  // point the snip tail node at the original dest target
  snip_tail->next = dest_node_orig_target;

  return rtn;
}

int main(int argc, char *argv[]) {
  // usage: prog_name NUM_MOVES SEED_STRING [optional CIRCLE_SIZE]

  int num_moves = atoi(argv[1]);
  printf("Moves needed: %i\n", num_moves);
  
  // either use the optional argument to "fill up" the circle with extra nodes,
  // or use the size of the provided seed string
  int num_custom_nodes = strlen(argv[2]);
  int num_nodes = (argc == 4) ? atoi(argv[3]) : num_custom_nodes;
  printf("Nodes needed: %i\n\n", num_nodes);
  
  // allocate nodes
  Node* nodes = (Node*)malloc(num_nodes * sizeof(Node));

  // allocate lookup table
  Node** nodes_by_label = (Node**)malloc(num_nodes * sizeof(Node*));
  
  // construct the ring
  Node* prev = &(nodes[num_nodes-1]);
  prev->next = &(nodes[0]);
  for (int i = 0; i < num_nodes; i++) {
    Node* n = &(nodes[i]);

    // previous node links to this one
    prev->next = n;

    // populate the value, either using the input or assuming
    int v;
    if (i >= num_custom_nodes) {
      v = i + 1;
    } else {
      v = (int)argv[2][i] - 48;
    }
    nodes[i].value = v;
    //populate lookup table
    nodes_by_label[v-1] = n;

    prev = &(nodes[i]);
  }

  // establish first node as current
  Node* current = &(nodes[0]);

  // do as many moves as requested
  for (int i = 0; i < num_moves; i++) {
    current = move(current, num_nodes, nodes_by_label);
  }
  printf("Final config:\n");
  dump_nodes(current);

  /* dump_nodes(current); */
  /* current = move(current, num_nodes); */
  /* dump_nodes(current); */
  
  // re-orient to node "1"
  current = find(1, nodes_by_label);

  // grab next two values. multiply and output (big)
  long long v1 = (long long int)(current->next->value);
  printf("v1 = %i\n", v1);
  long long v2 = (long long int)(current->next->next->value);
  printf("v2 = %i\n", v2);
  printf("product = %lld\n", v1 * v2);

  free(nodes);
}
