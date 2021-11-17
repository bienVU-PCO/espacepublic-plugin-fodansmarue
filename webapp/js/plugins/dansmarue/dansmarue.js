jQuery.noConflict();

(function($) {
    $(document).ready(function() {
        // fake background cover
        //$('.hero').css('background-image', 'url(' + $('.hero img').attr('src') + ')');

		$('input[name=radiobtn-grp]').each(function(index) {
			var node = $(this).closest('label').parent('div').attr('id');
			var idCategory = $('#typeSignalementId').val();
				if(node == idCategory){
					$(this).checked = true;
					var idRoot = $(this).parents('.modal.fade').attr('id');
					var selectedCategory = $('a[data-target=#'+idRoot+']');
					var selectedValue = $(this).closest('label').text();
					// check if item has parent
					
					if($(this).closest('ul').hasClass('child')) {
						var closestUl = $(this).closest('ul');
						var mergedLabel = closestUl.prev().text() + " : " + selectedValue;
						selectedCategory.find('.selected-value').text(mergedLabel);
						}
					else {
						selectedCategory.find('.selected-value').text(selectedValue);
					}
					if(!selectedCategory.hasClass('selected')){
						selectedCategory.addClass('selected');
					}
				}
				

		});
		
        var body = $('body');
        if (body.hasClass('step3')) {
            //hide noscript form
            $('form#noscript').hide();
            $('form#regular').show();

            var selectedCategory;
			
            $('form#regular a').click(function(){
                selectedCategory = $(this);
            });
            
            $('input[name=radiobtn-grp]').change(function() {
            	$('.btn-title-remarque2').removeAttr('disabled')
            });

            $('.btn-title-remarque2').click(function(){
                var selectedValue = $('input[name=radiobtn-grp]:checked').closest('label').text();

                if(typeof selectedValue !== "undefined") {
					$('[class="selected-value"]').text("");
					$('a.selected').removeClass('selected');
                    // check if item has parent
                    if($('input[name=radiobtn-grp]:checked').closest('ul').hasClass('child')) {
                        var closestUl = $('input[name=radiobtn-grp]:checked').closest('ul');

                        var mergedLabel = closestUl.prev().text() + " : " + selectedValue;
                        selectedCategory.find('.selected-value').text(mergedLabel);
                    }
                    else {
                        selectedCategory.find('.selected-value').text(selectedValue);
                    }

                    if(!selectedCategory.hasClass('selected')){
                        selectedCategory.addClass('selected');
                    }
                }
				var fillInput = selectedCategory.find('.type-remarque-texte').text() + " : "+ selectedCategory.find('.selected-value').text();
				$('#autocomplete').val(fillInput);
                var selectedCateroryId = $('input[name=radiobtn-grp]:checked').closest('label').parent('div').attr('id');
				$('#typeSignalementId').val(selectedCateroryId);
				var retour = $(this).parent('div').parent('div').parent('div').parent('div').attr('id'); 
                $( "#"+retour).modal('toggle');
                $('.btn-title-remarque2').attr('disabled', 'true');
            });


            $('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Expand this branch');			
            $('.tree li.parent_li > span').each(function(index) {
                var children = $(this).parent('li.parent_li').find(' > ul > li');

                children.hide();
                $(this).attr('title', 'Expand this branch').find('img').attr('src','images/image-DMR/selecteur-droit.png');

			});
            $('.tree li.parent_li > span').on('click', function (e) {
                var children = $(this).parent('li.parent_li').find(' > ul > li');
                if (children.is(":visible")) {
                    children.hide();
                    $(this).attr('title', 'Expand this branch').find('img').attr('src','images/image-DMR/selecteur-droit.png');
                }
                else {
                    children.show();
                    $(this).attr('title', 'Collapse this branch').find('img').attr('src','images/image-DMR/selecteur-bas.png');
                }
                e.stopPropagation();
            });

        }
		
    });
})(jQuery);