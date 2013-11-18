$( function () {
	$( '.bubbleInfo' ).each( function () {
		var distance = 10;
		var time = 250;
		var hideDelay = 100;

		var xPositionField = 190; // !!!
		var yPositionField = -100; // !!!

		var hideDelayTimer = null;

		var beingShown = false;
		var shown = false;
		var trigger = $( '.trigger', this );
		var info = $( '.popup', this ).css( 'opacity', 0 );

		$( [trigger.get( 0 ), info.get( 0 )] ).focusin( function () {
					if ( hideDelayTimer ) {
						clearTimeout( hideDelayTimer );
					}
					if ( beingShown || shown ) {
						return;
					} else {
						beingShown = true;

						info.css( {
							top: yPositionField,
							left: xPositionField,
							display: 'block'
						} ).animate( {
									top: '-=' + distance + 'px',
									opacity: 1
								}, time, 'swing', function() {
									beingShown = false;
									shown = true;
								} );
					}

					return false;
				} ).focusout( function () {
					if ( hideDelayTimer ) {
						clearTimeout( hideDelayTimer );
					}
					hideDelayTimer = setTimeout( function () {
						hideDelayTimer = null;
						info.animate( {
							top: '-=' + distance + 'px',
							opacity: 0
						}, time, 'swing', function () {
							shown = false;
							info.css( 'display', 'none' );
						} );

					}, hideDelay );

					return false;
				} );
	} );
} );
