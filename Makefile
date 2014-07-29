check:
	echo "Running make check..."
	lein check
	lein cloverage
	lein typed check
	lein typed coverage
	echo "make check complete."