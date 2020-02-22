BINARY=machine

VULNS=vuln1 vuln2 vuln3 vuln4 vuln5

VBINARIES=$(BINARY) $(VULNS:%=${BINARY}-%)

TARGETS=$(VBINARIES) $(VBINARIES:%=%-san) $(VBINARIES:%=%-fuzz) $(VBINARIES:%=%-cov)

HEADERS=$(wildcard *.h)

# allow the user to provide additional CFLAGS by doing e.g. CFLAGS=blah make
CFLAGS += -W -Wall -Wpedantic -Wno-language-extension-token

# allow the user to override what clang we use by doing e.g. CLANG=blah make
CLANG ?= clang-6.0
CC=$(CLANG)

SAN_FLAGS ?= -fsanitize=address -fno-omit-frame-pointer
FUZZ_FLAGS ?= -DMACHINE_LIBFUZZER -fsanitize=fuzzer,address -fno-omit-frame-pointer
NO_STRICT_OVERFLOW_CFLAGS ?= -fwrapv -fno-strict-overflow -Wstrict-overflow
COV_FLAGS ?= -fprofile-instr-generate -fcoverage-mapping

default: $(TARGETS)

.PHONY: default


%: %.c $(HEADERS)
	$(CC) $< $(CFLAGS) $(LDFLAGS) $(NO_STRICT_OVERFLOW_CFLAGS) -o $@ 


%-san: %.c $(HEADERS)
	$(CLANG) $< $(CFLAGS) $(LDFLAGS) $(SAN_FLAGS) $(NO_STRICT_OVERFLOW_CFLAGS) -o $@


# build a self-fuzzing binary with libFuzzer
# needs a recent clang version (e.g. clang-6.0)
# then to run it:
# ./machine-fuzz ~/tmp/corpus/ ~/tmp/findings/ -timeout=5 -only-ascii=1 -dict=libfuzzer-dict -max_total_time=1200 -print_final_stats=1
%-fuzz: %.c $(HEADERS)
	$(CLANG) $< $(CFLAGS) $(LDFLAGS) $(FUZZ_FLAGS) $(NO_STRICT_OVERFLOW_CFLAGS) -o $@

%-cov: %.c $(HEADERS)
	$(CLANG) $< $(CFLAGS) $(LDFLAGS) $(COV_FLAGS) $(NO_STRICT_OVERFLOW_CFLAGS) -o $@

clean:
	rm -f $(TARGETS) *.profraw *.profdata

