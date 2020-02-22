#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <ctype.h>
#include <stdbool.h>
#include <stddef.h>
#include "debug.h"
//vuln in line 255
/** add rd rs1 rs2   =~   rd = rs1 + rs2 */
const char INSTRUCTION_ADD[] = "add";

/** sub rd rs1 rs2   =~   rd = rs1 - rs2 */    
const char INSTRUCTION_SUBTRACT[] = "sub";

/** mul rd rs1 rs2   =~   rd = rs1 * rs2 */        
const char INSTRUCTION_MULT[] = "mul";

/** div rd rs1 rs2   =~   rd = rs1 / rs2 */            
const char INSTRUCTION_DIVIDE[] = "div";

/** ret rs           =~   return rs */
const char INSTRUCTION_RETURN[] = "ret";

/** ldr rd rs offs  =~    rd = rs[offs] */
const char INSTRUCTION_LOAD[] = "ldr";

/** str ra offs rb   =~    ra[offs] = rb */
const char INSTRUCTION_STORE[] = "str";

/** mov rd val       =~    rd = val */
const char INSTRUCTION_MOVE[] = "mov";

/** jmp offs         =~    pc = pc + offs */
const char INSTRUCTION_JUMP[] = "jmp";

/** jz ra offs       =~   if (ra == 0) pc = pc + offs else pc = pc + 1 */
const char INSTRUCTION_JZ[] = "jz";
    
#define NUM_REGS       32
#define MAX_REG        (NUM_REGS - 1)
#define MEMORY_SIZE    65536              /* 4 x as much memory as a 64 */
#define MAX_ADDR       (MEMORY_SIZE-1)


/* we force building with -fwrapv to ensure that signed overflow is defined
   to wrap around */
/* we dynamically allocate memory and regs since AdddressSanitizer appears
 * to more reliably catch out of bounds memory accesses for heap allocated
 * buffers than for global variables */
int32_t * memory = NULL;
int32_t * regs = NULL;

unsigned int count = 0; /* counts number of instructions executed so far */


static void machine_init(void){
  memory = malloc(sizeof(int32_t)*MEMORY_SIZE);
  regs = malloc(sizeof(int32_t)*NUM_REGS);
  memset(memory,0,sizeof(int32_t)*MEMORY_SIZE);
  memset(regs,0,sizeof(int32_t)*NUM_REGS);
  count = 0;
}

static void machine_free(void){
  free(memory);
  free(regs);
}

static void do_add(unsigned int dest, unsigned int src1, unsigned int src2)
{
  regs[dest] = regs[src1] + regs[src2];
}
    
static void do_sub(unsigned int dest, unsigned int src1, unsigned int src2)
{
  regs[dest] = regs[src1] - regs[src2];
}


static void do_mult(unsigned int dest, unsigned int src1, unsigned int src2)
{
  regs[dest] = regs[src1] * regs[src2];
}

/* returns 0 on success, nonzero on failure */
static int do_div(unsigned int dest, unsigned int src1, unsigned int src2)
{
  if (regs[src2] == 0){
    return -1;
  }else{
    regs[dest] = regs[src1] / regs[src2];
    return 0;
  }
}

/* returns 0 on success, nonzero on failure */
static int do_load(unsigned int dest, unsigned int src, int32_t offs){
  int32_t s = regs[src] + offs;

  if (s > MAX_ADDR){
    /* no op */
  }else if(s < 0){
    /* no op */
  }else{
    regs[dest] = memory[s];
  }
  return 0;
}

/* returns 0 on success, nonzero on failure */
static int do_store(unsigned int a, int32_t offs, unsigned int b){
  const int32_t s = regs[a] + offs;

  if (s > MAX_ADDR){
    /* no op */
  }else if(s < 0){
    /* no op */
  }else{
    memory[s] = regs[b];
  }
  return 0;  
}

static void do_move(unsigned int rd, int32_t val){
  regs[rd] = val;
}

#define valid_reg(reg) (reg >= 0 && reg <= MAX_REG)


/* returns 0 on success, nonzero on failure. puts register number into
 * the pointer reg */
static int parseReg(const char s[], unsigned int *reg)
{
  debug_printf("parseReg called on: %s\n",s);
  if (strlen(s) < 2 || strlen(s) > 3){
    debug_printf("parseReg: strlen %lu incorrect\n",strlen(s));
    return -1;
  }
  if (s[0] != 'r'){
    debug_printf("parseReg: first char not 'r'\n");
    return -1;
  }
  
  int num = 0;
  int res = sscanf(&(s[1]),"%d",&num);
  if (res != 1){
    return -1;
  }
  if (valid_reg(num)){
    *reg = (unsigned int)num;
    return 0;
  }else{
    return -1;
  }
}

/* FIXME: check assignment spec what is minimum address, e.g. -MAX_ADDR - 1 */
#define valid_offset(offset) (offset >= -MAX_ADDR && offset <= MAX_ADDR)

/* returns 0 on success, nonzero on failure. puts offset into
 * the pointer offset */
static int parseOffset(const char s[], int32_t *offset)
{
  int num = 0;
  int res = sscanf(&(s[0]),"%d",&num);
  if (res != 1){
    return -1;
  }
  if (valid_offset(num)){
    *offset = (int32_t)num;
    return 0;
  }else{
    return -1;
  }
}

const char WHITESPACE[] = " \t\r\n";


/* tokenise a string, splitting on characters in WHITESPACE, up to
 * a maxium of toksLen tokens, each of whose start addresses is put into
 * toks and each of which is NUL-terminated in str.
 * returns number of tokens found */
unsigned int tokenise(char *str, char * toks[], unsigned int toksLen){
  unsigned numToks = 0;
  while (numToks < toksLen){
    /* strip leading whitespace */     
    size_t start = strspn(str,WHITESPACE);
    debug_printf("start: %lu\n",start);
    if (str[start] != '\0'){
      toks[numToks] = &(str[start]);    
      debug_printf("Token %u starts here: --begintok--%s--end--\n",numToks,toks[numToks]);
    
      /* compute the length of the token */
      const size_t tokLen = strcspn(toks[numToks],WHITESPACE);
      if (tokLen > 0){
        toks[numToks][tokLen] = '\0';
        debug_printf("Found token %d: --begintok--%s--endtok--\n",numToks,toks[numToks]);
        str = &(toks[numToks][tokLen+1]);
        numToks++;
      }else{
        debug_printf("tokLen was: %lu\n",tokLen);
        return numToks;
      }
    }else{
      return numToks;
    }
  }
  return numToks;
}


#define MAX_LINE_LENGTH  1022
#define MAX_INSTRUCTIONS 65536

/* two extra chars in each line: the newline '\n' and NUL '\0' */
#define INSTRUCTION_LENGTH (MAX_LINE_LENGTH+2)
char program[MAX_INSTRUCTIONS][INSTRUCTION_LENGTH];

/* returns 0 on success (in which case res holds the result) or nonzero on
 * failure */
int execute(const unsigned int progLength, const int cycles, int32_t * res){
  int pc = 0;
  unsigned int cyclesExecuted = 0;
  while(cycles > 0 ? cyclesExecuted < (unsigned int)cycles : true){
    cyclesExecuted++;
    if (pc < 0 || pc >= (int)progLength){
      /* will cause an error but that is not a bug and
       * and indeed is what the VM is supposed to do if the pc becomes 
       * negative, since in this case the program's execution finishes 
       * early without a return value having been produced. */
      break;
    }

    /* take a copy of the instruction for us to edit -- this is inefficient
     * since we will do this every time we parse it but, whatever */
    char inst[INSTRUCTION_LENGTH];
    memcpy(inst,program[pc],INSTRUCTION_LENGTH);
    debug_printf("Parsing instruction: %s\n",inst);

    /* strip comment by terminating the string at the ; */
    char * const commentStart = strchr(inst,';');
    if (commentStart != NULL){
      *commentStart = '\0';
    }

    /* convert to lower */
    for (unsigned int i=0; inst[i] != '\0'; i++){
      inst[i] = tolower(inst[i]);
    }
    
    char * toks[4]; /* these are pointers to start of different tokens */
    const unsigned int numToks = tokenise(inst,toks,5);
    debug_printf("got %u tokens\n",numToks);
    
    if (numToks == 0){
      /* blank line */
      pc = pc + 1;
      count++;
      continue;
    }
    
    if (numToks < 2){
      return -1;
    }

    if (strcmp(toks[0],INSTRUCTION_ADD) == 0){
      if (numToks != 4){
        return -1;
      }
      unsigned int rd = 0;
      unsigned int rs1 = 0;
      unsigned int rs2 = 0;
      if (parseReg(toks[1],&rd) == 0 &&
          parseReg(toks[2],&rs1) == 0 &&
          parseReg(toks[3],&rs2) == 0){;
        do_add(rd,rs1,rs2);
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_SUBTRACT) == 0){
      if (numToks != 4){
        return -1;
      }
      unsigned int rd = 0;
      unsigned int rs1 = 0;
      unsigned int rs2 = 0;
      if (parseReg(toks[1],&rd) == 0 &&
          parseReg(toks[2],&rs1) == 0 &&
          parseReg(toks[3],&rs2) == 0){;
        do_sub(rd,rs1,rs2);
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_MULT) == 0){
      if (numToks != 4){
        return -1;
      }
      unsigned int rd = 0;
      unsigned int rs1 = 0;
      unsigned int rs2 = 0;
      if (parseReg(toks[1],&rd) == 0 &&
          parseReg(toks[2],&rs1) == 0 &&
          parseReg(toks[3],&rs2) == 0){;
        do_mult(rd,rs1,rs2);
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_DIVIDE) == 0){
      if (numToks != 4){
        return -1;
      }
      unsigned int rd = 0;
      unsigned int rs1 = 0;
      unsigned int rs2 = 0;
      if (parseReg(toks[1],&rd) == 0 &&
          parseReg(toks[2],&rs1) == 0 &&
          parseReg(toks[3],&rs2) == 0){;
        do_div(rd,rs1,rs2);
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_RETURN) == 0){
      if (numToks != 2){
        return -1;
      }
      unsigned int rs = 0;
      if (parseReg(toks[1],&rs) == 0){;
        count++;
        *res = regs[rs];
        return 0;
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_LOAD) == 0){
      if (numToks != 4){
        return -1;
      }
      unsigned int rd = 0;
      unsigned int rs = 0;
      int32_t offs = 0;
      if (parseReg(toks[1],&rd) == 0 &&
          parseReg(toks[2],&rs) == 0 &&
          parseOffset(toks[3],&offs) == 0){;
        do_load(rd,rs,offs);
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_STORE) == 0){
      if (numToks != 4){
        return -1;
      }
      unsigned int ra = 0;
      int32_t offs = 0;
      unsigned int rb = 0;
      if (parseReg(toks[1],&ra) == 0 &&
          parseOffset(toks[2],&offs) == 0 &&
          parseReg(toks[3],&rb) == 0){;
        do_store(ra,offs,rb);
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_MOVE) == 0){
      if (numToks != 3){
        return -1;
      }
      
      unsigned int rd = 0;
      int32_t offs = 0;

      if (parseReg(toks[1],&rd) == 0 &&
          parseOffset(toks[2],&offs) == 0){
        do_move(rd,offs);
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_JUMP) == 0){
      if (numToks != 2){
        return -1;
      }
      int32_t offs = 0;
      if (parseOffset(toks[1],&offs) == 0){
        /* this should never overflow given constraints on both pc and offs */
        pc  = pc + offs;
        count++;
        continue; /* avoid default increment of pc below */
      }else{
        return -1;
      }
    } else if (strcmp(toks[0],INSTRUCTION_JZ) == 0){
      if (numToks != 3){
        return -1;
      }
      unsigned int ra = 0; 
      int32_t offs = 0;

      if (parseReg(toks[1],&ra) == 0 &&
          parseOffset(toks[2],&offs) == 0){
        if (regs[ra] == 0){
          pc = pc + offs;
        }else{
          pc = pc + 1;
        }
        count++;
        continue; /* avoid default increment the pc below */
      } else {
        return -1;
      }
    }else{
      debug_printf("Unrecognised opcode: %s\n",toks[0]);
      return -1;
    }
    count++;
    pc = pc + 1;
  }
  
  debug_printf("Finishing pc: %d\n",pc);
  /* got here without returning already... */
  return -1;
}



/* returns the number of lines in the program when it is successfully read.
   Otherwise returns a negative result */
static int read_program(const char *filename){
  debug_printf("Attempting to read program. Max lines: %d, max line length: %d\n",MAX_INSTRUCTIONS,MAX_LINE_LENGTH);
  
  FILE *f = fopen(filename,"r");
  if (f == NULL){
    debug_printf("Couldn't open file %s for reading\n",filename);
    return -1;
  }
  memset(program,0,sizeof(program));

  int instructionCount = 0;
  while (instructionCount < MAX_INSTRUCTIONS){
    char * res = fgets(program[instructionCount],MAX_LINE_LENGTH+2,f);
    if (res == NULL){
      if (feof(f)){
        /* end of file */
        fclose(f);
        return instructionCount;
      }else{
        debug_printf("Error while reading, having read %d lines\n",instructionCount);
        fclose(f);
        return -1;
      }
    }
    if (program[instructionCount][MAX_LINE_LENGTH] != '\0'){
      if (!(program[instructionCount][MAX_LINE_LENGTH] == '\n' && program[instructionCount][MAX_LINE_LENGTH+1] == '\0')){
        debug_printf("Line %d exceeds maximum length (%d)\n",instructionCount+1,MAX_LINE_LENGTH);
        debug_printf("(Expected at array index %d to find NUL but found '%c' (%d))\n",MAX_LINE_LENGTH,program[instructionCount][MAX_LINE_LENGTH],program[instructionCount][MAX_LINE_LENGTH]);
        fclose(f);
        return -1;
      }
    }else{
      /* program[instructionCount][MAX_LINE_LENGTH] == '\0', so
         strlen is guaranteed to be <= MAX_LINE_LENGTH
         Check if it has a newline and add it if it needs it */
      size_t len = strlen(program[instructionCount]);
      if (len > 0){
        if (program[instructionCount][len-1] != '\n'){
          program[instructionCount][len] = '\n';
          program[instructionCount][len+1] = '\0';
        }
      }
    }
    instructionCount++;
  }
  
  if (feof(f)){
    /* final line of file didn't have a trailing newline */
    fclose(f);
    return instructionCount;
  }else{
    /* see if we are at end of file by trying to do one more read.
       this is necessary if the final line of the file ends in a 
       newline '\n' character */
    char c;
    int res = fread(&c,1,1,f);
    if (res == 1){
      debug_printf("Number of instructions (lines) in file exceeds max (%d)\n",MAX_INSTRUCTIONS);
      fclose(f);
      return -1;
    }else{
      if (feof(f)){
        /* final read found the EOF, so all good */
        fclose(f);
        return instructionCount;
      }else{
        /* probably won't ever get here */
        debug_printf("Error while trying to test if line %d was empty\n",instructionCount+1);
        fclose(f);
        return -1;
      }
    }
  }
}

/* returns exit code (0 on success)
 * cycles tells how many times through the execute loop to run for,
 * -1 means indefinitely */
int run_machine(const char * const filename, int cycles);

#ifdef MACHINE_LIBFUZZER
#include <stdint.h>
const char LIBFUZZER_INPUT_FILE[] = "libFuzzerInput.s";
#define FUZZER_CYCLES               5000 /* run for this many cycles when fuzzing */
/* turn off tracing to make it run faster */
#define printf(...)
#define fprintf(...)
int LLVMFuzzerTestOneInput(const uint8_t *Data, size_t Size) {
  FILE *f = fopen(LIBFUZZER_INPUT_FILE,"w");
  fwrite(Data,Size,1,f);
  fclose(f);
  run_machine(LIBFUZZER_INPUT_FILE,FUZZER_CYCLES);
  return 0; /* libFuzzer wants 0 returned always */
}
#else
int main(const int argc, const char * argv[]){
  if (argc != 2 && argc != 3){
    fprintf(stderr,"Usage: %s inputfile [cycles]\n",argv[0]);
    exit(1);
  }

  const char * const filename = argv[1];
  int cycles = -1; /* by default, run forever */
  if (argc > 2){
    int res = sscanf(argv[2],"%d",&cycles);
    
    if (res != 1){
      fprintf(stderr,"Value for cycles should be an integer. Got %s\n",argv[2]);
      exit(1);
    }
  }

  int res = run_machine(filename,cycles);
  return res; 
}
#endif

/* returns exit code (0 on success) */
int run_machine(const char * const filename, int cycles){
  machine_init();  

  const int res = read_program(filename);
  if (res < 0){
    fprintf(stderr,"Error reading program from %s\n",filename);
    machine_free();
    return 1;
  }

  const unsigned int numLines = res;
  debug_printf("Read program: %u lines\n",numLines);

  int32_t result;
  if (execute(numLines,cycles,&result) != 0){
    fprintf(stderr,"Program %s didn't execute successfully!\n",filename);
    machine_free();
    return 1;
  }
  
  printf("Program %s executed successfully. Result: %d\n",filename,result);
  machine_free();
  return 0;
}

