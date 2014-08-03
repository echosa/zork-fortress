check:
	echo "Running make check..."
	lein check
	lein test
	lein typed check
	echo "make check complete."
coverage:
	echo "Running make coverage..."
	lein check
	lein cloverage
	lein typed check
	lein typed coverage
	echo "make coverage complete."
