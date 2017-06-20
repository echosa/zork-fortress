check:
	echo "Running make check..."
	lein check
	lein test
	echo "make check complete."
coverage:
	echo "Running make coverage..."
	lein check
	lein cloverage
	echo "make coverage complete."
